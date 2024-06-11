package org.iot.hotelitybackend.hotelservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.hotelservice.service.StayService;
import org.iot.hotelitybackend.hotelservice.vo.RequestCheckinInfo;
import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;
import org.iot.hotelitybackend.hotelservice.vo.StaySearchCriteria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;
import static org.iot.hotelitybackend.common.util.ExcelType.STAY;
import static org.iot.hotelitybackend.common.util.ExcelUtil.createExcelFile;

@Slf4j
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

	/* 투숙 전체 내역 조회 (다중 조건 검색) */
	@GetMapping("/stays/page")
	public ResponseEntity<ResponseVO> selectStaysList(@ModelAttribute StaySearchCriteria criteria) {

		Map<String, Object> stayListInfo =
			stayService.selectStaysList(criteria);

		ResponseVO response = null;

		if (!stayListInfo.isEmpty()) {
			response = ResponseVO.builder()
				.data(stayListInfo)
				.resultCode(HttpStatus.OK.value())
				.message("조회 성공")
				.build();
		} else {
			response = ResponseVO.builder()
				.resultCode(HttpStatus.CONTINUE.value())
				.message("조회 실패")
				.build();
		}
		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 일자별 투숙 내역 리스트 조회 (메인페이지 대시보드용) */
	/* 날짜는 2024-06-14 형식으로 넣어줄 것. */
	@GetMapping("/stays/daily/{dateString}")
	public ResponseEntity<ResponseVO> selectStayByReservationCheckinDate(
		@PathVariable("dateString") String dateString) {
		Map<String, Object> stayListInfo = stayService.selectStayByReservationCheckinDate(dateString);
		ResponseVO response = ResponseVO.builder()
			.data(stayListInfo)
			.resultCode(HttpStatus.OK.value())
			.message("일자별 조회: \n"+ stayListInfo.get("dateString") + " 투숙 내역 조회 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 투숙 코드로 특정 투숙 내역 조회 */
	@GetMapping("stays/{stayCodePk}/selected")
	public ResponseEntity<ResponseVO> selectStayByStayCodePk(
		@PathVariable("stayCodePk") Integer stayCodePk) {

		Map<String, Object> stayInfo = stayService.selectStayByStayCodePk(stayCodePk);

		ResponseVO response = ResponseVO.builder()
			.data(stayInfo)
			.resultCode(HttpStatus.OK.value())
			.message(stayCodePk + "번 내역 조회 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 예약 체크인 선택 시 투숙 정보 생성 */
	@PostMapping("/stays/checkin")
	public ResponseEntity<ResponseVO> registStayByReservationCodePk(
		@RequestBody RequestCheckinInfo requestCheckinInfo) {

		int reservationCodeFk = requestCheckinInfo.getReservationCodeFk();
		int employeeCodeFk = requestCheckinInfo.getEmployeeCodeFk();
		int stayPeopleCount = requestCheckinInfo.getStayPeopleCount();

		ResponseVO response = null;

		try {
			Map<String, Object> registStayInfo =
				stayService.registStayByReservationCodePk(reservationCodeFk, employeeCodeFk, stayPeopleCount);

			if (registStayInfo.isEmpty()) {
				response = ResponseVO.builder()
					.resultCode(HttpStatus.CONFLICT.value())
					.message("이미 투숙 등록 된 예약입니다.")
					.build();
			} else {
				response = ResponseVO.builder()
					.data(registStayInfo)
					.resultCode(HttpStatus.CREATED.value())
					.message("예약 코드 " + reservationCodeFk + "번 투숙 등록 완료")
					.build();
			}
		} catch (Exception e) {
			response = ResponseVO.builder()
				.resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
		}

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 투숙 체크아웃 */
	/* 화면에서 체크아웃 할 투숙 내역을 체크 후 버튼을 눌렀을 때 */
	@PutMapping("/stays/{stayCodePk}/checkout")
	public ResponseEntity<ResponseVO> modifyStayCheckoutDate(@PathVariable("stayCodePk") Integer stayCodePk) {
		Map<String, Object> checkoutStayInfo = stayService.modifyStayCheckoutDate(stayCodePk);

		ResponseVO response = null;

		if (checkoutStayInfo.isEmpty()) {
			response = ResponseVO.builder()
				.resultCode(HttpStatus.CONFLICT.value())
				.message("이미 체크아웃 되었거나 해당 내역이 존재하지 않습니다.")
				.build();
		} else {
			response = ResponseVO.builder()
				.data(checkoutStayInfo)
				.resultCode(HttpStatus.OK.value())
				.message(stayCodePk + "번 투숙 체크아웃 완료")
				.build();
		}
		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 투숙 정보 수정 */
	@PutMapping("/stays/{stayCodePk}")
	public ResponseEntity<ResponseVO> modifyStayInfo(
		@RequestBody RequestModifyStay requestModifyStay,
		@PathVariable("stayCodePk") Integer stayCodePk) {
		Map<String, Object> modifyStay = stayService.modifyStayInfo(requestModifyStay, stayCodePk);

		ResponseVO response = ResponseVO.builder()
			.data(modifyStay)
			.resultCode(HttpStatus.CREATED.value())
			.message(stayCodePk + "번 투숙 정보 수정 완료")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 투숙 정보 삭제 */
	@DeleteMapping("/stays/{stayCodePk}")
	public ResponseEntity<ResponseVO> deleteStayInfo(@PathVariable("stayCodePk") Integer stayCodePk) {

		Map<String, Object> deleteStayInfo = stayService.deleteStay(stayCodePk);

		ResponseVO response = ResponseVO.builder()
			.data(deleteStayInfo)
			.resultCode(HttpStatus.NO_CONTENT.value())
			.message(stayCodePk + "번 투숙 정보 삭제 완료")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("stays/page/excel/download")
	public ResponseEntity<InputStreamResource> downloadStaysList(@ModelAttribute StaySearchCriteria criteria) {

		// 조회해서 DTO 리스트 가져오기
		Map<String, Object> stayListInfo =
			stayService.selectStaysList(criteria);

		try {

			// 엑셀 시트와 파일 만들기
			Map<String, Object> result = createExcelFile(
					(List<StayDTO>)stayListInfo.get(KEY_CONTENT),
					STAY.getFileName(),
					STAY.getHeaderStrings()
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
