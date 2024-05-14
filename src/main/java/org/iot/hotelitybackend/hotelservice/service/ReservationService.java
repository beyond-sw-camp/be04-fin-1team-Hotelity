package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReservationService {
	Map<String, Object> selectReservationListByMonth(int year, int month);

	Map<String, Object> selectReservationListByDay(LocalDateTime reservationCheckDate);

	Map<String, Object> selectReservationByReservationCodePk(int reservationCodePk);

	List<Integer> selectStaysList(int pageNum, String branchCodeFk, String roomCodeFk,
		LocalDateTime reservationCheckinDate, LocalDateTime reservationCheckoutDate);
}
