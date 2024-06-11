package org.iot.hotelitybackend.hotelservice.service;

import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;
import org.iot.hotelitybackend.hotelservice.vo.StaySearchCriteria;

import java.time.LocalDateTime;
import java.util.Map;

public interface StayService {
	Map<String, Object> selectStaysList(StaySearchCriteria criteria);

	Map<String, Object> registStayByReservationCodePk(int reservationCodeFk, int employeeCodeFk, int stayPeopleCount);

	Map<String, Object> modifyStayCheckoutDate(Integer stayCodePk);

    Map<String, Object> modifyStayInfo(RequestModifyStay requestModifyStay, Integer stayCodePk);

	Map<String, Object> deleteStay(int stayCodePk);

	Map<String, Object> selectStayByStayCodePk(Integer stayCodePk);

	Map<String, Object> selectStayByReservationCheckinDate(String dateString);
}
