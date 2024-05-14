package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface StayService {
	Map<String, Object> registStayByReservationCodePk(int reservationCodePk, int employeeCodeFk);

	Map<String, Object> selectStaysList(int pageNum, String branchCodeFk, String roomCodeFk, LocalDateTime reservationCheckinDate, LocalDateTime reservationCheckoutDate);

	Map<String, Object> selectStaysListByCustomerName(String customerName);

	Map<String, Object> modifyStayCheckoutDate(Integer stayCodePk);
}
