package org.iot.hotelitybackend.hotelservice.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.service.StayService;
import org.iot.hotelitybackend.hotelservice.vo.RequestModifyStay;
import org.iot.hotelitybackend.hotelservice.vo.RequestRegistStay;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<ResponseVO> selectStaysList(
		@RequestParam int pageNum,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) String roomCodeFk,
		@RequestParam(required = false) LocalDateTime reservationCheckinDate,
		@RequestParam(required = false) LocalDateTime reservationCheckoutDate
	) {

		Map<String, Object> stayListInfo =
			stayService.selectStaysList(pageNum, branchCodeFk, roomCodeFk, reservationCheckinDate, reservationCheckoutDate);

		ResponseVO response = ResponseVO.builder()
			.data(stayListInfo)
			.resultCode(HttpStatus.OK.value())
			.message("조회 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 예약 체크인 선택 시 투숙 정보 생성 */
	@PostMapping("/stays")
	public ResponseEntity<ResponseVO> registStayByReservationCodePk(
		@RequestParam int reservationCodePk,
		@RequestParam int employeeCodeFk) {

		Map<String, Object> registStayInfo = stayService.registStayByReservationCodePk(reservationCodePk, employeeCodeFk);

		ResponseVO response = null;

		if (registStayInfo.isEmpty()) {
			response = ResponseVO.builder()
				.resultCode(HttpStatus.CONFLICT.value())
				.message("이미 투숙 등록 된 예약입니다.")
				.build();
		} else {
			response = ResponseVO.builder()
				.data(registStayInfo)
				.resultCode(HttpStatus.CREATED.value())
				.message("예약 코드 " + reservationCodePk + "번 투숙 등록 완료")
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

		if(checkoutStayInfo.isEmpty()) {
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

	@PutMapping("/stays/{stayCodePk}")
	public ResponseEntity<ResponseVO> modifyStayInfo(
			@RequestBody RequestModifyStay requestModifyStay,
			@PathVariable("stayCodePk") Integer stayCodePk)
	{
		Map<String, Object> modifyStay = stayService.modifyStayInfo(requestModifyStay, stayCodePk);

		ResponseVO response = ResponseVO.builder()
				.data(modifyStay)
				.resultCode(HttpStatus.CREATED.value())
				.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@DeleteMapping("/stays/{stayCodePk}")
	public ResponseEntity<ResponseVO> deleteStayInfo(@PathVariable("stayCodePk") int stayCodePk) {

		Map<String, Object> deleteStayInfo = stayService.deleteStay(stayCodePk);

		ResponseVO response = ResponseVO.builder()
				.data(deleteStayInfo)
				.resultCode(HttpStatus.NO_CONTENT.value())
				.build();

		return ResponseEntity.status(response.getResultCode()).body(response);


	}
}
