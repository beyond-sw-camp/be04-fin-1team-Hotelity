package org.iot.hotelitybackend.chatgpt.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.iot.hotelitybackend.chatgpt.dto.ChatGPTRequest;
import org.iot.hotelitybackend.chatgpt.dto.ChatGPTResponse;
import org.iot.hotelitybackend.chatgpt.service.ChatGPTService;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/chat")
public class ChatGPTController {

	private ChatGPTService chatGPTService;

	@Autowired
	public ChatGPTController(ChatGPTService chatGPTService) {
		this.chatGPTService = chatGPTService;
	}

	// 오늘 예약 데이터 가져와서 분석
	@GetMapping("/reservations/daily")
	public ResponseEntity<ResponseVO> promptDailyReservations() {
		LocalDateTime now = LocalDateTime.now();
		Map<String, String> promptDataStringMap = chatGPTService.getReservationsDataOfToday(now);
		System.out.println(promptDataStringMap.get("reservationListData"));
		System.out.println(promptDataStringMap.get("contentType"));
		String chatGPTResponse = chatGPTService.getDailyChatGPTResponse(promptDataStringMap.get("reservationListData"), promptDataStringMap.get("contentType"));

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	// 오늘 결제 데이터 가져와서 분석
	@GetMapping("/payments/daily")
	public ResponseEntity<ResponseVO> promptDailyPayments() {
		LocalDateTime now = LocalDateTime.now();
		Map<String, String> promptDataStringMap = chatGPTService.getPaymentsDataOfToday(now);
		String chatGPTResponse = chatGPTService.getDailyChatGPTResponse(promptDataStringMap.get("paymentListData"), promptDataStringMap.get("contentType"));

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	// 이번달, 지난달, 작년의 이번달의 결제, 예약, 투숙 데이터 가져와서 분석
	@GetMapping("reservations/monthly")
	public ResponseEntity<ResponseVO> promptMonthlyReservations() {
		LocalDateTime now = LocalDateTime.now();
		String contentType = "예약";
		String promptDataString = chatGPTService.getDataMonth(now, contentType);
		System.out.println("promptDataString = " + promptDataString);
		String chatGPTResponse = chatGPTService.getMonthlyChatGPTResponse(promptDataString, contentType);

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	@GetMapping("payments/monthly")
	public ResponseEntity<ResponseVO> promptMonthlyPayments() {
		LocalDateTime now = LocalDateTime.now();
		String contentType = "결제";
		String promptDataString = chatGPTService.getDataMonth(now, contentType);
		System.out.println("promptDataString = " + promptDataString);
		String chatGPTResponse = chatGPTService.getMonthlyChatGPTResponse(promptDataString, contentType);

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	// 올해, 지난해의 결제, 예약, 투숙 데이터 가져와서 분석
	@GetMapping("/reservations/yearly")
	public ResponseEntity<ResponseVO> promptYearlyReservations() {
		String contentType = "예약";
		LocalDateTime now = LocalDateTime.now();
		String promptDataString = chatGPTService.getDataYear(now, contentType);
		String chatGPTResponse = chatGPTService.getYearlyChatGPTResponse(promptDataString, contentType);

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	@GetMapping("/payments/yearly")
	public ResponseEntity<ResponseVO> promptYearlyPayments() {
		String contentType = "결제";
		LocalDateTime now = LocalDateTime.now();
		String promptDataString = chatGPTService.getDataYear(now, contentType);
		String chatGPTResponse = chatGPTService.getYearlyChatGPTResponse(promptDataString, contentType);

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	private ResponseEntity<ResponseVO> getResponseVOResponseEntity(String chatGPTResponse) {
		ResponseVO responseVO;

		if (chatGPTResponse != null) {
			responseVO = ResponseVO.builder()
				.data(chatGPTResponse)
				.resultCode(HttpStatus.OK.value())
				.message("Prompt Success")
				.build();
		} else {
			responseVO = ResponseVO.builder()
				.data(null)
				.resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("Prompt Failed")
				.build();
		}
		return ResponseEntity.status(responseVO.getResultCode()).body(responseVO);
	}

	@GetMapping("/notices/daily")
	public ResponseEntity<ResponseVO> promptDailyNotices() {
		LocalDateTime now = LocalDateTime.now();
		String contentType = "공지";
		String promptDataString = chatGPTService.getDailyData(now, contentType);
		String chatGPTResponse = chatGPTService.getDailyChatGPTResponse(promptDataString, contentType);

		return getResponseVOResponseEntity(chatGPTResponse);
	}

	@GetMapping("vocs/daily")
	public ResponseEntity<ResponseVO> promptDailyVocs() {
		LocalDateTime now = LocalDateTime.now();
		String contentType = "VOC";
		String promptDataString = chatGPTService.getDailyData(now, contentType);
		String chatGPTResponse = chatGPTService.getDailyChatGPTResponse(promptDataString, contentType);

		return getResponseVOResponseEntity(chatGPTResponse);
	}
}