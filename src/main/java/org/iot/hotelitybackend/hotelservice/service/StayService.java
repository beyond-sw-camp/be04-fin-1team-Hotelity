package org.iot.hotelitybackend.hotelservice.service;

import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;

import java.time.LocalDateTime;
import java.util.Map;

public interface StayService {
	Map<String, Object> selectStaysList(int pageNum, String branchCodeFk, String roomLevelName, LocalDateTime stayCheckinTime, LocalDateTime stayCheckoutTime, String customerName);

	Map<String, Object> registStayByReservationCodePk(int reservationCodePk, int employeeCodeFk);

	Map<String, Object> modifyStayCheckoutDate(Integer stayCodePk);

    Map<String, Object> modifyStayInfo(RequestModifyStay requestModifyStay, Integer stayCodePk);

	Map<String, Object> deleteStay(int stayCodePk);
}
