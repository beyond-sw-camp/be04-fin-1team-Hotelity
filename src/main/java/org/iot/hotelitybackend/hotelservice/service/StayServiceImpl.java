package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.aggregate.CustomerSpecification;
import org.iot.hotelitybackend.customer.aggregate.NationEntity;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentSpecification;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StaySpecification;
import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.iot.hotelitybackend.hotelservice.repository.StayRepository;
import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;
import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StayServiceImpl implements StayService {

	private final StayRepository stayRepository;
	private final ModelMapper mapper;
	private final EmployeeRepository employeeRepository;
	private final ReservationRepository reservationRepository;
	private final CustomerRepository customerRepository;
	private final RoomRepository roomRepository;
	private final RoomCategoryRepository roomCategoryRepository;
	private final RoomLevelRepository roomLevelRepository;
	private final BranchRepository branchRepository;

	@Autowired
	public StayServiceImpl(StayRepository stayRepository, ModelMapper mapper, EmployeeRepository employeeRepository,
		ReservationRepository reservationRepository, CustomerRepository customerRepository,
		RoomRepository roomRepository, RoomCategoryRepository roomCategoryRepository,
		RoomLevelRepository roomLevelRepository, BranchRepository branchRepository) {
		this.stayRepository = stayRepository;
		this.mapper = mapper;
		this.employeeRepository = employeeRepository;
		this.reservationRepository = reservationRepository;
		this.customerRepository = customerRepository;
		this.roomRepository = roomRepository;
		this.roomCategoryRepository = roomCategoryRepository;
		this.roomLevelRepository = roomLevelRepository;
		this.branchRepository = branchRepository;
	}

	/* 투숙 내역 전체 조회(다중 조건 검색) */
	@Override
	public Map<String, Object> selectStaysList(int pageNum, String branchCodeFk, String roomLevelName,
		LocalDateTime stayCheckinTime, LocalDateTime stayCheckoutTime) {

		// 예약쪽에서 다중 조건 검색하여 결과값을 출력하고, 해당 예약 코드들을 전달
		// ReservationServiceImpl reservationService = new ReservationServiceImpl(
		// 	reservationRepository, mapper, customerRepository, roomRepository,
		// 	roomCategoryRepository, roomLevelRepository, branchRepository
		// );

		// List<Integer> reservationCodes =
		// 	reservationService.selectStaysList(
		// 		pageNum, branchCodeFk, roomLevelName, reservationCheckinDate, reservationCheckoutDate
		// 	);

		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("stayCheckinTime").descending());

		Specification<StayEntity> spec = Specification.where(null);

		// 지점명
		if(!branchCodeFk.isEmpty()) {
			List<BranchEntity> branch = branchRepository.findById(branchCodeFk).stream().toList();
			if (branch != null) {
				spec = spec.and(StaySpecification.equalsBranchCodeFk(branchCodeFk));
			}
		}

		// 객실 등급
		if(!roomLevelName.isEmpty()) {
			RoomLevelEntity roomLevel = roomLevelRepository.findByRoomLevelName(roomLevelName);
			if (roomLevel != null) {
				spec = spec.and(StaySpecification.equalsRoomLevelName(roomLevelName));
			}
		}

		// 체크인
		if(stayCheckinTime != null) {
			spec = spec.and(StaySpecification.equalsStayCheckinTime(stayCheckinTime));
		}

		// 체크아웃
		if(stayCheckoutTime != null) {
			spec = spec.and(StaySpecification.equalsStayCheckoutTime(stayCheckoutTime));
		}

		Page<StayEntity> stayPage = stayRepository.findAll(spec, pageable);
		List<StayEntity> stayEntityList = stayPage.stream().toList();
		List<StayDTO> stayDTOList = getFkColumnName(stayEntityList);

		// // forEach문 돌려서 List<StayDTO>에 값 추가하기

		Map<String, Object> stayPageInfo = new HashMap<>();
		stayPageInfo.put(KEY_TOTAL_PAGES_COUNT, stayPage.getTotalPages());
		stayPageInfo.put(KEY_CURRENT_PAGE_INDEX, stayPage.getNumber());
		stayPageInfo.put(KEY_CONTENT, stayDTOList);

		return stayPageInfo;
	}

	/* 예약 체크인 시 투숙 정보 등록 */
	@Transactional
	@Override
	public Map<String, Object> registStayByReservationCodePk(int reservationCodePk, int employeeCodeFk) {
		Map<String, Object> registStayInfo = new HashMap<>();

		// reservationCodeFk 중복 검사
		Optional<StayEntity> existingStayEntityOptional = stayRepository.findByReservationCodeFk(reservationCodePk);
		if (!existingStayEntityOptional.isPresent()) {
			ReservationServiceImpl reservationService = new ReservationServiceImpl(
				reservationRepository, mapper, customerRepository, roomRepository,
				roomCategoryRepository, roomLevelRepository, branchRepository
			);

			Map<String, Object> reservationInfo = reservationService.selectReservationByReservationCodePk(
				reservationCodePk);

			if (reservationInfo != null && !reservationInfo.isEmpty()) {
				// reservationInfo의 Value만 List에 담음
				List<ReservationDTO> reservationList = (List<ReservationDTO>)reservationInfo.get("content");

				StayEntity stayEntity = StayEntity.builder()
					.stayCheckinTime(LocalDateTime.now())
					.stayPeopleCount(reservationList.get(0).getReservationPersonnel())
					.employeeCodeFk(employeeCodeFk)
					.reservationCodeFk(reservationCodePk)
					.build();

				// stayDTO에 reservationList에 담긴 정보들을 담아 registStayInfo에 저장
				List<StayDTO> stayDTOList = new ArrayList<>();

				StayDTO stayDTO = new StayDTO();
				ReservationDTO reservationDTO = reservationList.get(0);

				stayDTO.setRoomCode(reservationDTO.getRoomCodeFk());
				stayDTO.setRoomLevelName(reservationDTO.getRoomLevelName());
				stayDTO.setRoomName(reservationDTO.getRoomName());
				stayDTO.setStayPeopleCount(reservationDTO.getReservationPersonnel());
				stayDTO.setStayCheckinTime(reservationDTO.getReservationCheckinDate());
				stayDTO.setEmployeeCodeFk(employeeCodeFk);
				stayDTO.setEmployeeName(employeeRepository.findById(employeeCodeFk).get().getEmployeeName());
				stayDTO.setBranchName(reservationDTO.getBranchCodeFk());
				stayDTO.setReservationCodeFk(reservationCodePk);

				stayDTOList.add(stayDTO);

				registStayInfo.put(KEY_CONTENT, mapper.map(stayRepository.save(stayEntity), StayDTO.class));
			}
		}
		return registStayInfo;
	}

	/* 투숙 체크아웃 */
	@Override
	public Map<String, Object> modifyStayCheckoutDate(Integer stayCodePk) {

		Map<String, Object> checkoutStayInfo = new HashMap<>();

		List<StayEntity> beforeCheckoutStayInfo = stayRepository.findById(stayCodePk).stream().toList();
		LocalDateTime isEmptyCheckoutTime = beforeCheckoutStayInfo.get(0).getStayCheckoutTime();

		// 입력한 투숙 코드가 존재하는지 확인
		if (!beforeCheckoutStayInfo.isEmpty()) {
			if (isEmptyCheckoutTime == null) {

				// 현재 시간을 가져오기
				LocalDateTime currentCheckoutDate = LocalDateTime.now();

				StayEntity beforeStayEntity = beforeCheckoutStayInfo.get(0);

				StayEntity checkoutStayEntity = StayEntity.builder()
					.stayCodePk(stayCodePk)
					.stayCheckinTime(beforeStayEntity.getStayCheckinTime())
					.stayCheckoutTime(currentCheckoutDate)
					.stayPeopleCount(beforeStayEntity.getStayPeopleCount())
					.employeeCodeFk(beforeStayEntity.getEmployeeCodeFk())
					.reservationCodeFk(beforeStayEntity.getReservationCodeFk())
					.build();

				stayRepository.save(checkoutStayEntity);

				List<StayDTO> stayDTOList = new ArrayList<>(selectStayByStayCodePk(stayCodePk));

				// System.out.println("========= stayDTOList 조회 =========");
				// for (StayDTO stayDTO1: stayDTOList) {
				// 	System.out.println(stayDTO1);
				// }

				checkoutStayInfo.put(KEY_CONTENT, stayDTOList);
			}
		}

		return checkoutStayInfo;
	}

	/* 투숙 정보 수정 */
	@Override
	public Map<String, Object> modifyStayInfo(RequestModifyStay requestModifyStay, Integer stayCodePk) {
		StayEntity stayEntity = StayEntity.builder()
				.stayCodePk(stayCodePk)
				.stayCheckinTime(requestModifyStay.getStayCheckinTime())
				.stayCheckoutTime(requestModifyStay.getStayCheckoutTime())
				.stayPeopleCount(requestModifyStay.getStayPeopleCount())
				.employeeCodeFk(requestModifyStay.getEmployeeCodeFk())
				.reservationCodeFk(requestModifyStay.getReservationCodeFk())
				.build();

		Map<String, Object> modifyStay = new HashMap<>();

		modifyStay.put(KEY_CONTENT, mapper.map(stayRepository.save(stayEntity), StayDTO.class));

		return modifyStay;
	}

	/* 투숙 정보 삭제 */
	@Override
	public Map<String, Object> deleteStay(int stayCodePk) {
		Map<String, Object> deleteStay = new HashMap<>();

		if (stayRepository.existsById(stayCodePk)) {
			stayRepository.deleteById(stayCodePk);
		} else {
			System.out.println("해당하는 투숙 정보가 없습니다.");
		}

		return deleteStay;
	}

	/* 투숙 코드로 조회(투숙 체크아웃용 메소드) */
	public List<StayDTO> selectStayByStayCodePk(Integer stayCodePk) {

		List<StayEntity> stayEntityList = stayRepository.findById(stayCodePk).stream().toList();

		// List<StayEntity> stayEntityList 조회
		for (StayEntity stayEntity : stayEntityList) {
			System.out.println("Stay Code: " + stayEntity.getStayCodePk());
			System.out.println("Check-in Time: " + stayEntity.getStayCheckinTime());
			System.out.println("Check-out Time: " + stayEntity.getStayCheckoutTime());
			System.out.println("People Count: " + stayEntity.getStayPeopleCount());
			System.out.println("Employee Code: " + stayEntity.getEmployeeCodeFk());
			System.out.println("Reservation Code: " + stayEntity.getReservationCodeFk());
			System.out.println("-----------------------------------");
		}

		List<StayDTO> stayDTOList = getFkColumnName(stayEntityList);

		for (StayDTO stayDTO : stayDTOList) {
			System.out.println(stayDTO);
		}

		return stayDTOList;
	}

	private List<StayDTO> getFkColumnName(List<StayEntity> stayEntityList) {
		List<StayDTO> list =
		stayEntityList.stream().map(stayEntity -> mapper.map(stayEntity, StayDTO.class))
			// 객실 코드
			.peek(stayDTO -> stayDTO.setRoomCode(
				mapper.map(reservationRepository.findById(stayDTO.getReservationCodeFk()), ReservationDTO.class)
					.getRoomCodeFk()))
			.peek(stayDTO -> stayDTO.setRoomLevelName(
				roomLevelRepository.findById(
					roomCategoryRepository.findById(
						roomRepository.findById(
							reservationRepository.findById(
								stayDTO.getReservationCodeFk()
							).get().getRoomCodeFk()
						).get().getRoomCategoryCodeFk()
					).get().getRoomLevelCodeFk()
				).get().getRoomLevelName()))
			// 객실명
			.peek(stayDTO -> stayDTO.setRoomName(
				roomCategoryRepository.findById(
					roomRepository.findById(
						reservationRepository.findById(
							stayDTO.getReservationCodeFk()
						).get().getRoomCodeFk()
					).get().getRoomCategoryCodeFk()
				).get().getRoomName()))
			// 직원명
			.peek(stayDTO -> stayDTO.setEmployeeName(
				mapper.map(employeeRepository.findById(stayDTO.getEmployeeCodeFk()), EmployeeDTO.class)
					.getEmployeeName()))
			// 지점명
			.peek(stayDTO -> stayDTO.setBranchName(
				reservationRepository.findById(stayDTO.getReservationCodeFk()).get().getBranchCodeFk()))
			.toList();

		return list;
	}
}
