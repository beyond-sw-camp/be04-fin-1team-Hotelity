package org.iot.hotelitybackend.hotelservice.service;

import java.util.Map;

public interface StayService {
	Map<String, Object> registStayByReservationCodePk(int reservationCodePk, int employeeCodeFk);
}
