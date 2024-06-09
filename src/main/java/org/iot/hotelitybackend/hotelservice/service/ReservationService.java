package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelservice.vo.ReservationSearchCriteria;

public interface ReservationService {
	Map<String, Object> selectReservationListByMonth(
		int year,
		int month,
		ReservationSearchCriteria criteria
	);

	Map<String, Object> selectReseravtionInfoByReservationCodePk(Integer reservationCodePk);

	Map<String, Object> selectReservationListByDay(LocalDateTime reservationCheckDate);

	Map<String, Object> selectReservationsByYear(Integer yearInput);

	Map<String, Object> selectLatestReservationList();
}
