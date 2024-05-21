package org.iot.hotelitybackend.smpt;

import java.util.List;
import java.util.Map;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

	private JavaMailSender mailSender;
	private static final String FROM_ADDRESS = "eodud3196@gs.cwnu.ac.kr";

	@Override
	public Map<String, Object> mailsend(List<RequestDTO> requestDTOList) {
		SimpleMailMessage message = new SimpleMailMessage();

		for (RequestDTO dto : requestDTOList) {
			message.setTo(dto.getAddress());
			// message.setFrom(EmailServiceImpl.FROM_ADDRESS);
			message.setSubject(dto.getTitle());
			message.setText(dto.getMessage());

			mailSender.send(message);
		}

		return null;
	}
}
