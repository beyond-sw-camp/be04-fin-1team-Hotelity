package org.iot.hotelitybackend.chatgpt.controller;

import java.time.LocalDateTime;

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

	@GetMapping("/daily")
	public ResponseEntity<ResponseVO> promptDailyData() {
		LocalDateTime now = LocalDateTime.now();
		String promptDataString = chatGPTService.getDataOfToday(now);
		String chatGPTResponse = chatGPTService.getDailyChatGPTResponse(promptDataString);

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

	@GetMapping("/monthly")
	public ResponseEntity<ResponseVO> promptMonthlyData() {
		LocalDateTime now = LocalDateTime.now();
		String promptDataString = chatGPTService.getDataMonth(now);
		String chatGPTResponse = chatGPTService.getMonthlyChatGPTResponse(promptDataString);

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
}