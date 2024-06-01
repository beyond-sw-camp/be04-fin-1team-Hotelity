package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReservationService {
	Map<String, Object> selectReservationListByMonth(
		int year, int month,
		Integer reservationCodePk,
		Integer customerCodeFk,
		String customerName,
		String customerEnglishName,
		String roomCodeFk,
		String roomName,
		String roomLevelName,
		Integer roomCapacity,
		String branchCodeFk,
		LocalDateTime reservationDate,
		LocalDateTime reservationCheckinDate,
		LocalDateTime reservationCheckoutDate,
		Integer reservationCancelStatus,
		String orderBy, Integer sortBy
	);

	Map<String, Object> selectReseravtionInfoByReservationCodePk(Integer reservationCodePk);

	Map<String, Object> selectReservationListByDay(LocalDateTime reservationCheckDate);

	Map<String, Object> selectReservationsByYear(Integer yearInput);
}
