package org.iot.hotelitybackend.hotelservice.controller;

import static org.iot.hotelitybackend.common.constant.Constant.*;
import static org.iot.hotelitybackend.common.util.ExcelType.*;
import static org.iot.hotelitybackend.common.util.ExcelUtil.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.service.PaymentService;
import org.iot.hotelitybackend.hotelservice.vo.PaymentSearchCriteria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public ResponseEntity<ResponseVO> selectPaymentLogListWithFilter(@ModelAttribute PaymentSearchCriteria criteria) {

		Map<String, Object> paymentLogInfo =
			paymentService.selectPaymentLogList(
				criteria
			);

		ResponseVO response = ResponseVO.builder()
			.data(paymentLogInfo)
			.resultCode(HttpStatus.OK.value())
			.message("조회 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("/payments/excel/download")
	public ResponseEntity<InputStreamResource> downloadPaymentLogExcel(@ModelAttribute PaymentSearchCriteria criteria) {

		try {
			// 조회해서 DTO 리스트 가져오기
			Map<String, Object> paymentLogInfo =
				paymentService.selectPaymentLogList(
					criteria
				);

			// 엑셀 시트와 파일 만들기
			Map<String, Object> result = createExcelFile(
				(List<PaymentDTO>)paymentLogInfo.get(KEY_CONTENT),
				PAYMENT.getFileName(),
				PAYMENT.getHeaderStrings()
			);

			return ResponseEntity
				.ok()
				.headers((HttpHeaders)result.get("headers"))
				.body(new InputStreamResource((ByteArrayInputStream)result.get("result")));
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
