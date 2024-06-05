package org.iot.hotelitybackend.hotelmanagement.controller;

import lombok.extern.slf4j.Slf4j;
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

import java.io.ByteArrayInputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;
import static org.iot.hotelitybackend.common.util.ExcelType.ANCILLARY;
import static org.iot.hotelitybackend.common.util.ExcelUtil.createExcelFile;

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
	public ResponseEntity<ResponseVO> selectAllFacilities(
		@RequestParam(required = false) Integer pageNum,
		@RequestParam(required = false) Integer ancillaryCodePk,
		@RequestParam(required = false) String ancillaryName,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) String ancillaryLocation,
		@RequestParam(required = false) LocalTime ancillaryOpenTime,
		@RequestParam(required = false) LocalTime ancillaryCloseTime,
		@RequestParam(required = false) String ancillaryPhoneNumber,
		@RequestParam(required = false) Integer ancillaryCategoryCodeFk,
		@RequestParam(required = false) String branchName,
		@RequestParam(required = false) String ancillaryCategoryName
	) {
		Map<String, Object> facilityPageInfo = ancillaryService.selectAllFacilities(
			pageNum, ancillaryCodePk, ancillaryName, branchCodeFk, ancillaryLocation, ancillaryOpenTime, ancillaryCloseTime, ancillaryPhoneNumber, ancillaryCategoryCodeFk, branchName, ancillaryCategoryName
		);

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
	public ResponseEntity<InputStreamResource> downloadAllFacilitiesExcel(
		@RequestParam(required = false) Integer pageNum,
		@RequestParam(required = false) Integer ancillaryCodePk,
		@RequestParam(required = false) String ancillaryName,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) String ancillaryLocation,
		@RequestParam(required = false) LocalTime ancillaryOpenTime,
		@RequestParam(required = false) LocalTime ancillaryCloseTime,
		@RequestParam(required = false) String ancillaryPhoneNumber,
		@RequestParam(required = false) Integer ancillaryCategoryCodeFk,
		@RequestParam(required = false) String branchName,
		@RequestParam(required = false) String ancillaryCategoryName,
		@RequestParam(required = false) String orderBy,
		@RequestParam(required = false) Integer sortBy
	) {
		try {

			// 조회해서 DTO 리스트 가져오기
			Map<String, Object> facilityListInfo = ancillaryService.selectAllFacilities(
				pageNum, ancillaryCodePk, ancillaryName, branchCodeFk, ancillaryLocation, ancillaryOpenTime, ancillaryCloseTime, ancillaryPhoneNumber, ancillaryCategoryCodeFk, branchName, ancillaryCategoryName
			);

			// 엑셀 시트와 파일 만들기
			Map<String, Object> result = createExcelFile(
					(List<AncillaryDTO>)facilityListInfo.get(KEY_CONTENT),
					ANCILLARY.getFileName(),
					ANCILLARY.getHeaderStrings()
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
