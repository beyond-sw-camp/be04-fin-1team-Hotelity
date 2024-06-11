package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.iot.hotelitybackend.hotelservice.vo.PaymentSearchCriteria;
import org.springframework.web.bind.annotation.RequestParam;

public interface PaymentService {

	Map<String, Object> selectPaymentLogList(PaymentSearchCriteria criteria);
}
