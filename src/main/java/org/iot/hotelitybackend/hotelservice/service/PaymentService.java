package org.iot.hotelitybackend.hotelservice.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface PaymentService {

	Map<String, Object> selectPaymentLogList(
		int pageNum, Integer paymentCodePk, Integer customerCodePk, String customerName, LocalDateTime paymentDate,
		Integer paymentCancelStatus, String paymentMethod, Integer reservationCodeFk,
		Integer paymentTypeCodeFk, String paymentTypeName, String orderBy, Integer sortBy
	);
}
