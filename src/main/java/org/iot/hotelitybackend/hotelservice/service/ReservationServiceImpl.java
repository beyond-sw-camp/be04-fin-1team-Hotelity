package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final ModelMapper mapper;
	private final CustomerRepository customerRepository;
	private final RoomRepository roomRepository;
	private final RoomCategoryRepository roomCategoryRepository;
	private final RoomLevelRepository roomLevelRepository;
	private final BranchRepository branchRepository;

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository, ModelMapper mapper,
		CustomerRepository customerRepository, RoomRepository roomRepository,
		RoomCategoryRepository roomCategoryRepository,
		RoomLevelRepository roomLevelRepository, BranchRepository branchRepository) {
		this.reservationRepository = reservationRepository;
		this.mapper = mapper;
		this.customerRepository = customerRepository;
		this.roomRepository = roomRepository;
		this.roomCategoryRepository = roomCategoryRepository;
		this.roomLevelRepository = roomLevelRepository;
		this.branchRepository = branchRepository;
	}

	/* 월별 예약 리스트 전체 조회 */
	@Override
	public Map<String, Object> selectReservationListByMonth(int year, int month) {

		// 특정 월의 예약 내역을 조회하기 위해 월의 시작일과 종료일을 지정
		LocalDateTime startOfMonth =
			LocalDateTime.of(year, month, 1, 0, 0);
		System.out.println("해당 월의 시작 일자: " + startOfMonth);

		LocalDateTime endOfMonth =
			LocalDateTime.of(year, month, startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear()),
				23, 59, 59);
		System.out.println("해당 월의 마지막 일자: " + endOfMonth);

		// 특정 월에 해당하는 예약 내역 리스트 조회
		List<ReservationEntity> reservationList = reservationRepository.findByReservationCheckinDateBetween(startOfMonth, endOfMonth);

		List<ReservationDTO> reservationDTOList = getFkColumnsName(reservationList);

		Map<String, Object> reservationListInfo = new HashMap<>();

		reservationListInfo.put(KEY_CONTENT, reservationDTOList);

		return reservationListInfo;
	}

	/* 일자별 예약 리스트 조회 */
	@Override
	public Map<String, Object> selectReservationListByDay(LocalDateTime reservationCheckDate) {
		List<ReservationEntity> dailyReservationList = reservationRepository.findByReservationCheckinDate(reservationCheckDate);

		List<ReservationDTO> dailyReservationDTOList = getFkColumnsName(dailyReservationList);

		Map<String, Object> dailyReservationInfo = new HashMap<>();

		dailyReservationInfo.put(KEY_CONTENT, dailyReservationDTOList);

		return dailyReservationInfo;
	}

	/* 예약 코드로 검색 */
	@Override
	public Map<String, Object> selectReservationByReservationCodePk(int reservationCodePk) {

		List<ReservationEntity> reservationListByCode = reservationRepository.findById(reservationCodePk).stream().toList();;

		List<ReservationDTO> reservationDTOListByCode = getFkColumnsName(reservationListByCode);

		Map<String, Object> searchReservationInfoByCode = new HashMap<>();

		searchReservationInfoByCode.put(KEY_CONTENT, reservationDTOListByCode);

		return searchReservationInfoByCode;
	}

	/* fk 값들의 이름을 가져오는 코드 */
	private List<ReservationDTO> getFkColumnsName(List<ReservationEntity> reservationList) {

		List<ReservationDTO> list =
			reservationList.stream().map(reservationEntity -> mapper.map(reservationEntity, ReservationDTO.class))
				.peek(reservationDTO -> reservationDTO.setCustomerName(
					mapper.map(customerRepository.findById(reservationDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()))
				.peek(reservationDTO -> reservationDTO.setRoomName(String.valueOf(roomCategoryRepository.findById(
					roomRepository.findById(reservationDTO.getRoomCodeFk()).get().getRoomCategoryCodeFk()
				).get().getRoomName())))
				.peek(reservationDTO -> reservationDTO.setRoomLevelName(
						roomLevelRepository.findById(
							roomCategoryRepository.findById(
								roomRepository.findById(
									reservationDTO.getRoomCodeFk()
								).get().getRoomCategoryCodeFk()
							).get().getRoomLevelCodeFk()
						).get().getRoomLevelName()
					)
				).toList();

		return list;
	}
}
