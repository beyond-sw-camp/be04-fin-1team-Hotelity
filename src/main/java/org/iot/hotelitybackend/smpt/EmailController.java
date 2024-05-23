package org.iot.hotelitybackend.smpt;

import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/mail")
	public ResponseEntity<ResponseVO> execMail(@RequestBody RequestDTO requestDTO){
		Map<String, Object> execMail = emailService.mailsend(requestDTO);

		ResponseVO response = ResponseVO.builder()
			.data(execMail)
			.resultCode(HttpStatus.OK.value())
			.message("이메일 발송 완료")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);

	}
}
