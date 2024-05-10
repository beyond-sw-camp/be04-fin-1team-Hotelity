package org.iot.hotelitybackend.hotelservice.controller;

import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel-service")
public class PaymentController {

	private final PaymentService paymentService;
	private final ModelMapper mapper;

	@Autowired
	public PaymentController(PaymentService paymentService, ModelMapper mapper) {
		this.paymentService = paymentService;
		this.mapper = mapper;
	}

	@GetMapping("/page")
	public ResponseEntity<ResponseVO> selectPaymentLogList(@RequestParam int pageNum) {
		Map<String, Object> paymentLogInfo = paymentService.selectPaymentLogList(pageNum);

		ResponseVO response = ResponseVO.builder()
			.data(paymentLogInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
