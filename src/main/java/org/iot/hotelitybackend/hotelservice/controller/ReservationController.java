package org.iot.hotelitybackend.hotelservice.controller;

import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel-service")
public class ReservationController {

	private final ReservationService reservationService;
	private final ModelMapper mapper;

	@Autowired
	public ReservationController(ReservationService reservationService, ModelMapper mapper) {
		this.reservationService = reservationService;
		this.mapper = mapper;
	}




}
