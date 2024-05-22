package org.iot.hotelitybackend.hotelmanagement.controller;

import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelmanagement.service.AncillaryService;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyFacility;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistFacility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/facilities")
	public ResponseEntity<ResponseVO> registFacility(RequestRegistFacility requestRegistFacility) {
		Map<String, Object> registFacilityInfo = ancillaryService.registFacility(requestRegistFacility);

		ResponseVO response = ResponseVO.builder()
				.data(registFacilityInfo)
				.resultCode(HttpStatus.CREATED.value())
				.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@PutMapping("/facilities/{ancillaryCodePk}")
	public ResponseEntity<ResponseVO> modifyFacility(
			@RequestBody RequestModifyFacility requestModifyFacility,
			@PathVariable ("ancillaryCodePk") int ancillaryCodePk
	) {
		Map<String, Object> modifyFacilityInfo = ancillaryService.modifyFacilityInfo(requestModifyFacility, ancillaryCodePk);

		ResponseVO response = ResponseVO.builder()
				.data(modifyFacilityInfo)
				.resultCode(HttpStatus.CREATED.value())
				.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@DeleteMapping("/facilities/{ancillaryCodePk}")
	public ResponseEntity<ResponseVO> deleteFacility(@PathVariable("ancillaryCodePk") int ancillaryCodePk) {

		Map<String, Object> deleteFacilityInfo = ancillaryService.deleteFacilityInfo(ancillaryCodePk);

		ResponseVO response = ResponseVO.builder()
				.data(deleteFacilityInfo)
				.resultCode(HttpStatus.NO_CONTENT.value())
				.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
