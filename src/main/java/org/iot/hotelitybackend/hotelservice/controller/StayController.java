package org.iot.hotelitybackend.hotelservice.controller;

import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.service.StayService;
import org.iot.hotelitybackend.hotelservice.vo.RequestRegistStay;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel-service")
public class StayController {

	private final StayService stayService;
	private final ModelMapper mapper;

	@Autowired
	public StayController(StayService stayService, ModelMapper mapper) {
		this.stayService = stayService;
		this.mapper = mapper;
	}

	/* 예약 체크인 선택 시 투숙 정보 생성 */
	@PostMapping("/stays")
	public ResponseEntity<ResponseVO> registStayByReservationCodePk(
		@RequestParam int reservationCodePk
		, @RequestParam int employeeCodeFk) {

		Map<String, Object> registStayInfo = stayService.registStayByReservationCodePk(reservationCodePk, employeeCodeFk);

		ResponseVO response = ResponseVO.builder()
			.data(registStayInfo)
			.resultCode(HttpStatus.CREATED.value())
			.message("예약 코드 " + reservationCodePk + "번 투숙 등록됨")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
