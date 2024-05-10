package org.iot.hotelitybackend.hotelservice.service;

import java.util.Map;

public interface PaymentService {
	Map<String, Object> selectPaymentLogList(int pageNum);
}
