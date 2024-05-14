package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.iot.hotelitybackend.hotelservice.repository.StayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	private BranchRepository branchRepository;

	@Autowired
	public StayServiceImpl(StayRepository stayRepository, ModelMapper mapper, EmployeeRepository employeeRepository,
		ReservationRepository reservationRepository, CustomerRepository customerRepository,
		RoomRepository roomRepository,
		RoomCategoryRepository roomCategoryRepository, RoomLevelRepository roomLevelRepository) {
		this.stayRepository = stayRepository;
		this.mapper = mapper;
		this.employeeRepository = employeeRepository;
		this.reservationRepository = reservationRepository;
		this.customerRepository = customerRepository;
		this.roomRepository = roomRepository;
		this.roomCategoryRepository = roomCategoryRepository;
		this.roomLevelRepository = roomLevelRepository;
	}

	/* 투숙 내역 전체 조회(다중 조건 검색) */
	@Override
	public Map<String, Object> selectStaysList(int pageNum, String branchCodeFk, String roomCodeFk,
		LocalDateTime reservationCheckinDate, LocalDateTime reservationCheckoutDate) {

		// 예약쪽에서 다중 조건 검색하여 결과값을 출력하고, 해당 예약 코드들을 전달
		ReservationServiceImpl reservationService = new ReservationServiceImpl(
			reservationRepository, mapper, customerRepository, roomRepository,
			roomCategoryRepository, roomLevelRepository, branchRepository
		);

		List<Integer> reservationCodes =
			reservationService.selectStaysList(
				pageNum, branchCodeFk, roomCodeFk, reservationCheckinDate, reservationCheckoutDate
			);

		// forEach문 돌려서 List<StayDTO>에 값 추가하기

		return null;
	}

	/* 고객이름별 투숙 내역 조회 */
	@Override
	public Map<String, Object> selectStaysListByCustomerName(String customerName) {



		return null;
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

			Map<String, Object> reservationInfo = reservationService.selectReservationByReservationCodePk(reservationCodePk);

			if (reservationInfo != null && !reservationInfo.isEmpty()) {
				// reservationInfo의 Value만 List에 담음
				List<ReservationDTO> reservationList = (List<ReservationDTO>) reservationInfo.get("content");

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

		List<StayEntity> stayCheckout = stayRepository.findById(stayCodePk).stream().toList();


		return null;
	}
}
