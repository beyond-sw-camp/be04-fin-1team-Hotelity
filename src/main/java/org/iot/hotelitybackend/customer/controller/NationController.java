package org.iot.hotelitybackend.customer.controller;

import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.iot.hotelitybackend.customer.dto.NationDTO;
import org.iot.hotelitybackend.customer.service.NationService;
import org.iot.hotelitybackend.customer.vo.ResponseNation;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nations")
public class NationController {
	private final ModelMapper mapper;
	private final NationService nationService;

	public NationController(ModelMapper mapper, NationService nationService) {
		this.mapper = mapper;
		this.nationService = nationService;
	}

	@GetMapping()
	public ResponseEntity<List<ResponseNation>> selectNationList() {
		List<NationDTO> responseNation = nationService.selectNationList();

		List<ResponseNation> responseNation1 = mapper.map(responseNation, List.class);
		if (responseNation != null) {
			return ResponseEntity.ok(responseNation1);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
}
