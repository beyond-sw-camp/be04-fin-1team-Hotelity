package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public interface ReservationService {
	Map<String, Object> selectReservationListByMonth(int year, int month);
}
