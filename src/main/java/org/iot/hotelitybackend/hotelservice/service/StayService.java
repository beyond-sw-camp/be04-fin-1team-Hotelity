package org.iot.hotelitybackend.hotelservice.service;

import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;

import java.time.LocalDateTime;
import java.util.Map;

public interface StayService {
	Map<String, Object> selectStaysList(
		Integer pageNum, Integer stayCodePk, Integer customerCodeFk,
		String customerName, String roomCodeFk, String roomName,
		String roomLevelName, Integer roomCapacity, Integer stayPeopleCount,
		LocalDateTime stayCheckinTime, LocalDateTime stayCheckoutTime,
		String branchCodeFk, Integer employeeCodeFk, String employeeName,
		Integer reservationCodeFk, Integer stayCheckoutStatus,
		String orderBy, Integer sortBy);

	Map<String, Object> registStayByReservationCodePk(int reservationCodeFk, int employeeCodeFk, int stayPeopleCount);

	Map<String, Object> modifyStayCheckoutDate(Integer stayCodePk);

    Map<String, Object> modifyStayInfo(RequestModifyStay requestModifyStay, Integer stayCodePk);

	Map<String, Object> deleteStay(int stayCodePk);

	Map<String, Object> selectStayByStayCodePk(Integer stayCodePk);
}
