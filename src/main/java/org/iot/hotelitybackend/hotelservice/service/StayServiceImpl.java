package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StaySpecification;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.iot.hotelitybackend.hotelservice.repository.StayRepository;
import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;
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
	public Map<String, Object> selectStaysList(
		int pageNum, Integer stayCodePk, Integer customerCodeFk,
		String customerName, String roomCodeFk, String roomName,
		String roomLevelName, Integer roomCapacity, Integer stayPeopleCount,
		LocalDateTime stayCheckinTime, LocalDateTime stayCheckoutTime,
		String branchCodeFk, Integer employeeCodeFk, String employeeName,
		Integer reservationCodeFk, Integer stayCheckoutStatus,
		String orderBy, Integer sortBy) {

		Pageable pageable;

		if (orderBy == null) {
			pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("stayCheckinTime").descending());
		} else {
			if (sortBy == 1) {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
			}
			else{
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
			}
		}
		Specification<StayEntity> spec = Specification.where(null);

		// 투숙코드
		if (stayCodePk != null) {
			spec = spec.and(StaySpecification.equalsStayCodePk(stayCodePk));
		}

		// 고객코드
		if (customerCodeFk != null) {
			spec = spec.and(StaySpecification.equalsCustomerCodeFk(customerCodeFk));
		}

		// 고객이름
		if (customerName != null) {
			spec = spec.and(StaySpecification.likeCustomerName(customerName));
		}

		// 객실코드
		if (roomCodeFk != null) {
			spec = spec.and(StaySpecification.equalsRoomCodeFk(roomCodeFk));
		}

		// 객실명
		if (roomName != null) {
			spec = spec.and(StaySpecification.likeRoomName(roomName));
		}

		// 객실 등급명
		if (roomLevelName != null && !roomLevelName.isEmpty()) {
			spec = spec.and(StaySpecification.likeRoomLevelName(roomLevelName));
		}
		// StaySpecification의 Join이 제대로 동작하는지 확인
		// List<StayEntity> stays = stayRepository.findAll(spec);
		//
		// for (StayEntity stay : stays) {
		// 	RoomEntity room = stay.getReservation().getRoom();
		// 	RoomCategoryEntity roomCategory = room.getRoomCategory();
		// 	RoomLevelEntity roomLevel = roomCategory.getRoomLevel();
		// 	System.out.println("Stay ID: " + stay.getStayCodePk());
		// 	System.out.println("Room ID: " + room.getRoomCodePk());
		// 	System.out.println("Room Category: " + roomCategory.getRoomCategoryCodePk());
		// 	System.out.println("Room Level: " + roomLevel.getRoomLevelName());
		// 	System.out.println("====================");
		// }

		// 객실 수용 인원
		// if (roomCapacity != null) {
		// 	spec = spec.and(StaySpecification.equalsRoomCapacity(roomCapacity));
		// }

		// 투숙 인원
		if (stayPeopleCount != null) {
			spec = spec.and(StaySpecification.equalsStayPeopleCount(stayPeopleCount));
		}

		// 체크인 날짜
		if (stayCheckinTime != null) {
			spec = spec.and(StaySpecification.equalsStayCheckinTime(stayCheckinTime));
		}

		// 체크아웃 날짜
		if (stayCheckoutTime != null) {
			spec = spec.and(StaySpecification.equalsStayCheckoutTime(stayCheckoutTime));
		}

		// 지점코드
		if (branchCodeFk != null) {
			spec = spec.and(StaySpecification.equalsBranchCodeFk(branchCodeFk));
		}

		// 직원코드
		if (employeeCodeFk != null) {
			spec = spec.and(StaySpecification.equalsEmployeeCodeFk(employeeCodeFk));
		}

		// 직원이름
		if (employeeName != null) {
			spec = spec.and(StaySpecification.likeEmployeeName(employeeName));
		}

		// 예약코드
		if (reservationCodeFk != null) {
			spec = spec.and(StaySpecification.equalsReservationCodeFk(reservationCodeFk));
		}

		Page<StayEntity> stayPage = stayRepository.findAll(spec, pageable);
		List<StayEntity> stayEntityList = stayPage.getContent();
		List<StayDTO> stayDTOList = setDTOField(stayEntityList);

		Map<String, Object> stayPageInfo = new HashMap<>();
		stayPageInfo.put(KEY_TOTAL_PAGES_COUNT, stayPage.getTotalPages());
		stayPageInfo.put(KEY_CURRENT_PAGE_INDEX, stayPage.getNumber());
		stayPageInfo.put(KEY_CONTENT, stayDTOList);

		return stayPageInfo;
	}

	/* 투숙 코드로 특정 투숙 조회 */
	@Override
	public Map<String, Object> selectStayByStayCodePk(Integer stayCodePk) {

		List<StayEntity> stayEntityList = stayRepository.findById(stayCodePk).stream().toList();

		List<StayDTO> stayDTOList = setDTOField(stayEntityList);

		Map<String, Object> stayInfo = new HashMap<>();
		stayInfo.put(KEY_CONTENT, stayDTOList);

		return stayInfo;
	}

	/* 예약 체크인 시 투숙 정보 등록 */
	@Transactional
	@Override
	public Map<String, Object> registStayByReservationCodePk(int reservationCodeFk, int employeeCodeFk,
		int stayPeopleCount) {
		Map<String, Object> registStayInfo = new HashMap<>();

		ReservationServiceImpl reservationService = new ReservationServiceImpl(
			reservationRepository, mapper, customerRepository, roomRepository,
			roomCategoryRepository, roomLevelRepository, branchRepository
		);

		// 예약 정보 가져오기
		List<ReservationEntity> reservationInfo =
			reservationRepository.findById(reservationCodeFk).stream().toList();

		if (!reservationInfo.isEmpty()) {

			// reservationCodeFk 중복 검사
			List<StayEntity> existingStayEntity =
				stayRepository.findByReservationCodeFk(reservationCodeFk).stream().toList();

			if (existingStayEntity.isEmpty()) {

				// ReservationDTO Field Mapping
				List<ReservationDTO> reservationDTOList = reservationService.setDTOField(reservationInfo);
				ReservationDTO reservationDTO = reservationDTOList.get(0);

				// 예약 정보를 StayEntity에 저장
				StayEntity stayEntity = StayEntity.builder()
					.stayCheckinTime(LocalDateTime.now())
					.stayPeopleCount(stayPeopleCount)
					.employeeCodeFk(employeeCodeFk)
					.reservationCodeFk(reservationCodeFk)
					.build();

				stayRepository.save(stayEntity);

				// StayDTO Field Mapping
				StayDTO stayDTO = setStayDTO(stayEntity, reservationDTO, reservationCodeFk, employeeCodeFk);

				registStayInfo.put(KEY_CONTENT, stayDTO);
			}
		} else {
			System.out.println("===== 해당 예약 코드가 존재하지 않습니다. =====");
			throw new RuntimeException("Reservation code:" + reservationCodeFk + " 해당 예약 코드가 존재하지 않습니다.");
		}
		return registStayInfo;
	}

	private StayDTO setStayDTO(
		StayEntity stayEntity, ReservationDTO reservationDTO, int reservationCodeFk, int employeeCodeFk) {

		StayDTO stayDTO = new StayDTO();

		stayDTO.setStayCodePk(stayEntity.getStayCodePk());
		stayDTO.setCustomerCodeFk(reservationDTO.getCustomerCodeFk());
		stayDTO.setCustomerName(reservationDTO.getCustomerName());
		stayDTO.setRoomCode(reservationDTO.getRoomCodeFk());
		stayDTO.setRoomName(reservationDTO.getRoomName());
		stayDTO.setRoomLevelName(reservationDTO.getRoomLevelName());
		stayDTO.setRoomCapacity(reservationDTO.getRoomCapacity());
		stayDTO.setStayPeopleCount(reservationDTO.getReservationPersonnel());
		stayDTO.setStayCheckinTime(LocalDateTime.now());
		stayDTO.setEmployeeCodeFk(employeeCodeFk);
		stayDTO.setPICEmployeeName(employeeRepository.findById(employeeCodeFk).get().getEmployeeName());
		stayDTO.setBranchCodeFk(reservationDTO.getBranchCodeFk());
		stayDTO.setReservationCodeFk(reservationCodeFk);

		return stayDTO;
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

				List<StayDTO> stayDTOList = new ArrayList<>(getStayByStayCodePk(stayCodePk));

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

		Integer reservationCodeFk = requestModifyStay.getReservationCodeFk();
		List<StayEntity> stayEntityList =
			stayRepository.findByReservationCodeFk(reservationCodeFk).stream().toList();

		StayEntity stayEntity = StayEntity.builder()
			.stayCodePk(stayCodePk)
			.stayCheckinTime(requestModifyStay.getStayCheckinTime())
			.stayCheckoutTime(requestModifyStay.getStayCheckoutTime())
			.stayPeopleCount(requestModifyStay.getStayPeopleCount())
			.employeeCodeFk(requestModifyStay.getEmployeeCodeFk())
			.reservationCodeFk(requestModifyStay.getReservationCodeFk())
			.build();

		stayRepository.save(stayEntity);

		List<StayDTO> stayDTOList = setDTOField(stayEntityList);

		StayDTO stayDTO = new StayDTO();

		Map<String, Object> modifyStay = new HashMap<>();

		modifyStay.put(KEY_CONTENT, stayDTOList);

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
	public List<StayDTO> getStayByStayCodePk(Integer stayCodePk) {

		List<StayEntity> stayEntityList = stayRepository.findById(stayCodePk).stream().toList();

		// List<StayEntity> stayEntityList 조회
		// for (StayEntity stayEntity : stayEntityList) {
		// 	System.out.println("Stay Code: " + stayEntity.getStayCodePk());
		// 	System.out.println("Check-in Time: " + stayEntity.getStayCheckinTime());
		// 	System.out.println("Check-out Time: " + stayEntity.getStayCheckoutTime());
		// 	System.out.println("People Count: " + stayEntity.getStayPeopleCount());
		// 	System.out.println("Employee Code: " + stayEntity.getEmployeeCodeFk());
		// 	System.out.println("Reservation Code: " + stayEntity.getReservationCodeFk());
		// 	System.out.println("-----------------------------------");
		// }

		List<StayDTO> stayDTOList = setDTOField(stayEntityList);

		for (StayDTO stayDTO : stayDTOList) {
			System.out.println(stayDTO);
		}

		return stayDTOList;
	}

	private List<StayDTO> setDTOField(List<StayEntity> stayEntityList) {
		List<StayDTO> list =
			stayEntityList.stream().map(stayEntity -> mapper.map(stayEntity, StayDTO.class))
				.peek(stayDTO -> stayDTO.setCustomerCodeFk(
					mapper.map(reservationRepository.findById(stayDTO.getReservationCodeFk()), ReservationDTO.class)
						.getCustomerCodeFk()))
				.peek(stayDTO -> stayDTO.setCustomerName(
					customerRepository.findById(
						reservationRepository.findById(stayDTO.getReservationCodeFk())
							.get().getCustomerCodeFk()
					).get().customerName))
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
				.peek(stayDTO -> stayDTO.setPICEmployeeName(
					employeeRepository.findById(stayDTO.getEmployeeCodeFk())
						.get().getEmployeeName()))
				// 지점명
				.peek(stayDTO -> stayDTO.setBranchCodeFk(
					reservationRepository.findById(stayDTO.getReservationCodeFk()).get().getBranchCodeFk()))
				// 고객코드
				.peek(stayDTO -> stayDTO.setCustomerCodeFk(
					reservationRepository.findById(stayDTO.getReservationCodeFk()).get().getCustomerCodeFk()))
				// 고객이름
				.peek(stayDTO -> stayDTO.setCustomerName(
					customerRepository.findById(
						reservationRepository.findById(stayDTO.getReservationCodeFk()
						).get().getCustomerCodeFk()
					).get().customerName))
				// 객실수용인원
				.peek(stayDTO -> stayDTO.setRoomCapacity(
					roomCategoryRepository.findById(
						roomRepository.findById(
							reservationRepository.findById(stayDTO.getReservationCodeFk()
							).get().getRoomCodeFk()
						).get().getRoomCategoryCodeFk()
					).get().getRoomCapacity()))
				.toList();

		// System.out.println("========= stayDTOList 조회 =========");
		// for (StayDTO stayDTO1 : list) {
		// 	System.out.println(stayDTO1);
		// }

		return list;
	}
}
