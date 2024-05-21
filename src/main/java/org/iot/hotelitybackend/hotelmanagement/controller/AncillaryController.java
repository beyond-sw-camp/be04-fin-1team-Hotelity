package org.iot.hotelitybackend.hotelmanagement.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelmanagement.dto.AncillaryDTO;
import org.iot.hotelitybackend.hotelmanagement.service.AncillaryService;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyFacility;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistFacility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		@PathVariable("ancillaryCodePk") int ancillaryCodePk
	) {
		Map<String, Object> modifyFacilityInfo = ancillaryService.modifyFacilityInfo(requestModifyFacility,
			ancillaryCodePk);

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

	@GetMapping("facilities/excel/download")
	public ResponseEntity<InputStreamResource> downloadAllFacilitiesExcel() {
		try {
			List<AncillaryDTO> ancillaryDTOList = ancillaryService.selectAllFacilitiesForExcel();
			Map<String, Object> result = ancillaryService.createFacilitiesExcelFile(ancillaryDTOList);

			return ResponseEntity
				.ok()
				.headers((HttpHeaders)result.get("headers"))
				.body(new InputStreamResource((ByteArrayInputStream)result.get("result")));

		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
