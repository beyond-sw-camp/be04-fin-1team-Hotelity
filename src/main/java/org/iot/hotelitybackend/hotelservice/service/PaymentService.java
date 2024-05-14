package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public interface PaymentService {
	Map<String, Object> selectPaymentLogList(int pageNum, Integer customerCodeFk, LocalDateTime paymentDate, Integer paymentCancelStatus);

}
