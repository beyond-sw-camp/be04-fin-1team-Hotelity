package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface ReservationService {
	Map<String, Object> selectReservationListByMonth(int year, int month);

	Map<String, Object> selectReservationListByDay(LocalDateTime reservationCheckDate);

	Map<String, Object> selectReservationByReservationCodePk(int reservationCodePk);
}
