package org.iot.hotelitybackend.smpt;

import lombok.RequiredArgsConstructor;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/mail")
	public ResponseEntity<ResponseVO> execMail(@RequestBody List<RequestDTO> requestDTOList){
		Map<String, Object> execMail = emailService.mailsend(requestDTOList);

		ResponseVO response = ResponseVO.builder()
			.data(execMail)
			.resultCode(HttpStatus.OK.value())
			.message("이메일 발송 완료")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);

	}
}
