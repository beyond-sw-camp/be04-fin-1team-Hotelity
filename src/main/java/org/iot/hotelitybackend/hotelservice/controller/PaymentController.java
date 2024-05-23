package org.iot.hotelitybackend.hotelservice.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	/* 다중 조건 검색을 적용한 전체 결제 내역 리스트 조회 */
	@GetMapping("/payments/page")
	public ResponseEntity<ResponseVO> selectPaymentLogListWithFilter(
		@RequestParam int pageNum,
		@RequestParam(required = false) Integer customerCodeFk,
		@RequestParam(required = false) String customerName,
		@RequestParam(required = false) LocalDateTime paymentDate,
		@RequestParam(required = false) Integer paymentCancelStatus,
		@RequestParam(required = false) String paymentMethod,
		@RequestParam(required = false) Integer reservationCodeFk,
		@RequestParam(required = false) Integer paymentTypeCodeFk,
		@RequestParam(required = false) String paymentTypeName,
		@RequestParam(required = false) String orderBy,
		@RequestParam(required = false) Integer sortBy) {

		Map<String, Object> paymentLogInfo =
			paymentService.selectPaymentLogList(
				pageNum, customerCodeFk, customerName, paymentDate, paymentCancelStatus,
				paymentMethod, reservationCodeFk, paymentTypeCodeFk, paymentTypeName,
				orderBy, sortBy
			);

		ResponseVO response = ResponseVO.builder()
			.data(paymentLogInfo)
			.resultCode(HttpStatus.OK.value())
			.message("조회 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
