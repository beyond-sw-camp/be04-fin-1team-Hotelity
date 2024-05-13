package org.iot.hotelitybackend.hotelservice.controller;

import org.iot.hotelitybackend.hotelservice.service.StayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	// @PostMapping("/stays")
	// public
}
