package org.iot.hotelitybackend.hotelservice.controller;

import java.util.Date;
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

	/* 전체 결제 내역 리스트 조회 */
	@GetMapping("/payments/page")
	public ResponseEntity<ResponseVO> selectPaymentLogList(@RequestParam int pageNum) {
		Map<String, Object> paymentLogInfo = paymentService.selectPaymentLogList(pageNum);

		ResponseVO response = ResponseVO.builder()
			.data(paymentLogInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 다중 조건 검색을 적용한 전체 결제 내역 리스트 조회 */
	@GetMapping("/payments/list")
	public ResponseEntity<ResponseVO> selectPaymentLogListWithFilter(
		  @RequestParam int pageNum
		, @RequestParam(value = "customerName", required = false) String searchCustomerName
		, @RequestParam(value = "paymentTypeName", required = false) String searchPaymentTypeName
		, @RequestParam(value = "paymentCancleStatus", required = false) int searchPaymentCancleStatus) {

		Map<String, Object> paymentLogInfo = paymentService.selectPaymentLogList(pageNum);

		ResponseVO response = ResponseVO.builder()
			.data(paymentLogInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("/payments")
	public ResponseEntity<ResponseVO> selectPaymentByPaymentDate(@PathVariable Date paymentDate) {

		Map<String, Object> paymentLogInfo = paymentService.selectPaymentByPaymentDate(paymentDate);

		ResponseVO response = ResponseVO.builder()
			.data(paymentLogInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
