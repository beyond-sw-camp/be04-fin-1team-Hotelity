package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
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
	private final BranchRepository branchRepository;

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository, ModelMapper mapper,
		CustomerRepository customerRepository, RoomRepository roomRepository, BranchRepository branchRepository) {
		this.reservationRepository = reservationRepository;
		this.mapper = mapper;
		this.customerRepository = customerRepository;
		this.roomRepository = roomRepository;
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

		return null;
	}
}
