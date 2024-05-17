package org.iot.hotelitybackend.hotelservice.service;

import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;

import java.time.LocalDateTime;
import java.util.Map;

public interface StayService {
	Map<String, Object> registStayByReservationCodePk(int reservationCodePk, int employeeCodeFk);

	Map<String, Object> selectStaysList(int pageNum, String branchCodeFk, String roomCodeFk, LocalDateTime reservationCheckinDate, LocalDateTime reservationCheckoutDate);

	Map<String, Object> modifyStayCheckoutDate(Integer stayCodePk);

    Map<String, Object> modifyStayInfo(RequestModifyStay requestModifyStay, Integer stayCodePk);

	Map<String, Object> deleteStay(int stayCodePk);
}
