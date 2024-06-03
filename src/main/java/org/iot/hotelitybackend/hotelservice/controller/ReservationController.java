package org.iot.hotelitybackend.hotelservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;
import static org.iot.hotelitybackend.common.util.ExcelType.RESERVATION;
import static org.iot.hotelitybackend.common.util.ExcelUtil.createExcelFile;

@Slf4j
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

	/* 월별 예약 리스트 전체 조회 */
	/* 해당 월에 예약된 전체 리스트(페이징 처리 x)를 프론트로 넘겨주면
	 * 프론트에서 해당 리스트를 받아 날짜를 기준으로 예약 건 수를 카운트 하여 캘린더에 출력 */
	@GetMapping("/reservations/{reservationCheckinDate}")
	public ResponseEntity<ResponseVO> selectReservationListByMonth(
		@PathVariable("reservationCheckinDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reservationCheckinDate,
		@RequestParam(required = false) Integer reservationCodePk,
		@RequestParam(required = false) Integer customerCodeFk,
		@RequestParam(required = false) String customerName,
		@RequestParam(required = false) String customerEnglishName,
		@RequestParam(required = false) String roomCodeFk,
		@RequestParam(required = false) String roomName,
		@RequestParam(required = false) String roomLevelName,
		@RequestParam(required = false) Integer roomCapacity,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) LocalDateTime reservationDate,
		@RequestParam(required = false) LocalDateTime reservationCheckInDate,
		@RequestParam(required = false) LocalDateTime reservationCheckoutDate,
		@RequestParam(required = false) Integer reservationCancelStatus,
		@RequestParam(required = false) String orderBy,
		@RequestParam(required = false) Integer sortBy) {
		int year = reservationCheckinDate.getYear();
		int month = reservationCheckinDate.getMonthValue();

		Map<String, Object> reservationInfo =
			reservationService.selectReservationListByMonth(
				year, month,
				reservationCodePk, customerCodeFk,
				customerName,customerEnglishName,
				roomCodeFk,roomName,
				roomLevelName,roomCapacity,
				branchCodeFk,reservationDate,
				reservationCheckInDate, reservationCheckoutDate,
				reservationCancelStatus, orderBy, sortBy
			);

		ResponseVO response = ResponseVO.builder()
			.data(reservationInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 예약 시간 순서대로 최신 3개 조회 (대시보드용) */
	@GetMapping("/reservations/latest")
	public ResponseEntity<ResponseVO> selectLatestReservationList() {
		Map<String, Object> latestReservationInfo = reservationService.selectLatestReservationList();

		ResponseVO response = ResponseVO.builder()
			.data(latestReservationInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("/reservations/year/{yearInput}")
	public ResponseEntity<ResponseVO> selectReservationsByYear(@PathVariable("yearInput") Integer yearInput) {

		Map<String, Object> reservationInfo = reservationService.selectReservationsByYear(yearInput);

		ResponseVO response = ResponseVO.builder()
			.data(reservationInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 예약 코드로 특정 예약 내역 조회 */
	@GetMapping("reservations/{reservationCodePk}/selected")
	public ResponseEntity<ResponseVO> selectReservationInfoByReservationCodePk(
		@PathVariable("reservationCodePk") Integer reservationCodePk) {

		Map<String, Object> reservationInfo =
			reservationService.selectReseravtionInfoByReservationCodePk(reservationCodePk);

		ResponseVO response = ResponseVO.builder()
			.data(reservationInfo)
			.resultCode(HttpStatus.OK.value())
			.message("조회 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 일자별 예약 내역 리스트 조회 */
	/* 캘린더에서 특정 일자 선택 시 조회되는 리스트 */
	/* => 프론트에서 월별 리스트의 값을 처리하여 일별로 나누어 list에 append 할 것 */
	@GetMapping("reservations/{reservationCheckinDate}/day")
	public ResponseEntity<ResponseVO> selectReservationListByDay(
		@PathVariable("reservationCheckinDate") LocalDateTime reservationCheckDate) {
		Map<String, Object> dailyReservationInfo = reservationService.selectReservationListByDay(reservationCheckDate);

		int year = reservationCheckDate.getYear();
		int month = reservationCheckDate.getMonthValue();
		int day = reservationCheckDate.getDayOfMonth();

		ResponseVO response = ResponseVO.builder()
			.data(dailyReservationInfo)
			.resultCode(HttpStatus.OK.value())
			.message(year + "/" + month + "/" + day + " 예약 내역 조회")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("/reservations/{reservationCheckinDate}/excel/download")
	public ResponseEntity<InputStreamResource> downloadReservationListByMonth(
		@PathVariable("reservationCheckinDate") LocalDateTime reservationCheckinDate,
		@RequestParam(required = false) Integer reservationCodePk,
		@RequestParam(required = false) Integer customerCodeFk,
		@RequestParam(required = false) String customerName,
		@RequestParam(required = false) String customerEnglishName,
		@RequestParam(required = false) String roomCodeFk,
		@RequestParam(required = false) String roomName,
		@RequestParam(required = false) String roomLevelName,
		@RequestParam(required = false) Integer roomCapacity,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) LocalDateTime reservationDate,
		@RequestParam(required = false) LocalDateTime reservationCheckInDate,
		@RequestParam(required = false) LocalDateTime reservationCheckoutDate,
		@RequestParam(required = false) Integer reservationCancelStatus,
		@RequestParam(required = false) String orderBy,
		@RequestParam(required = false) Integer sortBy
	) {
		int year = reservationCheckinDate.getYear();
		int month = reservationCheckinDate.getMonthValue();

		// 조회해서 DTO 리스트 가져오기
		Map<String, Object> reservationInfo =
			reservationService.selectReservationListByMonth(
				year, month,
				reservationCodePk, customerCodeFk,
				customerName, customerEnglishName,
				roomCodeFk, roomName,
				roomLevelName, roomCapacity,
				branchCodeFk, reservationDate,
				reservationCheckInDate, reservationCheckoutDate,
				reservationCancelStatus, orderBy, sortBy
			);

		try {

			Map<String, Object> resultExcel = createExcelFile(
					(List<ReservationDTO>)reservationInfo.get(KEY_CONTENT),
					RESERVATION.getFileName(),
					RESERVATION.getHeaderStrings()
			);

			return ResponseEntity
				.ok()
				.headers((HttpHeaders)resultExcel.get("headers"))
				.body(new InputStreamResource((ByteArrayInputStream)resultExcel.get("result")));

		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
