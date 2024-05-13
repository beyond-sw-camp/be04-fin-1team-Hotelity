package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
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
		List<ReservationEntity> reservationInfo = reservationRepository.findByReservationCheckinDateBetween(startOfMonth, endOfMonth);

		// 조회 확인용
		System.out.println("예약 내역 : ");
		for (ReservationEntity reservation : reservationInfo) {
			System.out.println("Reservation Code: " + reservation.getReservationCodePk());
			System.out.println("Reservation Date: " + reservation.getReservationDate());
			System.out.println("Check-in Date: " + reservation.getReservationCheckinDate());
			System.out.println("Check-out Date: " + reservation.getReservationCheckoutDate());
			System.out.println("Customer Code: " + reservation.getCustomerCodeFk());
			System.out.println("Room Code: " + reservation.getRoomCodeFk());
			System.out.println("Branch Code: " + reservation.getBranchCodeFk());
			System.out.println("Cancel Status: " + reservation.getReservationCancelStatus());
			System.out.println("Personnel: " + reservation.getReservationPersonnel());
			System.out.println("---------------------------------------------");
		}

		// 객실명: 예약(객실코드fk) -> 객실(객실카테고리코드fk) -> 객실카테고리.getRoomName

		// 객실등급명:
		List<ReservationDTO> reservationDTOList =
			reservationInfo.stream().map(reservationEntity -> mapper.map(reservationEntity, ReservationDTO.class))
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

		System.out.println("reservatopmDTOList: ");
		for (ReservationDTO reservationDTO : reservationDTOList) {
			System.out.println("Reservation Code: " + reservationDTO.getReservationCodePk());
			System.out.println("Reservation Date: " + reservationDTO.getReservationDate());
			System.out.println("Check-in Date: " + reservationDTO.getReservationCheckinDate());
			System.out.println("Check-out Date: " + reservationDTO.getReservationCheckoutDate());
			System.out.println("Customer Code: " + reservationDTO.getCustomerCodeFk());
			System.out.println("Customer Name: " + reservationDTO.getCustomerName());
			System.out.println("Room Code: " + reservationDTO.getRoomCodeFk());
			System.out.println("Room Name: " + reservationDTO.getRoomName());
			System.out.println("Room Level Name: " + reservationDTO.getRoomLevelName());
			System.out.println("Branch Code: " + reservationDTO.getBranchCodeFk());
			System.out.println("Reservation Cancel Status: " + reservationDTO.getReservationCancelStatus());
			System.out.println("Reservation Personnel: " + reservationDTO.getReservationPersonnel());
			System.out.println();
		}

		// .peek(reservationDTO -> {
		// 	String roomCategoryCode = mapper.map(
		// 			roomRepository.findById(reservationDTO.getRoomCodeFk()),
		// 			RoomDTO.class
		// 		)
		// 		.getRoomCategoryCodeFk()
		// 		.orElse(""); // orElse에는 기본값 설정 가능
		//
		// 	reservationDTO.setRoomName(
		// 		roomCategoryRepository.findById(roomCategoryCode)
		// 	);
		// })


		return null;
	}
}
