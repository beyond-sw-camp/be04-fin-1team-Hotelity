package org.iot.hotelitybackend.hotelmanagement.controller;

import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelmanagement.service.AncillaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel-management")
public class AncillaryController {
	private AncillaryService ancillaryService;
	private ModelMapper mapper;

	@Autowired
	public AncillaryController(AncillaryService ancillaryService, ModelMapper mapper) {
		this.ancillaryService = ancillaryService;
		this.mapper = mapper;
	}

	@GetMapping("/facilities")
	public ResponseEntity<ResponseVO> selectAllFacilities(@RequestParam int pageNum) {
		Map<String, Object> facilityPageInfo = ancillaryService.selectAllFacilities(pageNum);

		ResponseVO response = ResponseVO.builder()
			.data(facilityPageInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
