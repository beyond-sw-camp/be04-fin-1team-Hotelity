package org.iot.hotelitybackend.smpt;

import java.util.Map;

public interface EmailService {
	Map<String, Object> mailsend(RequestDTO requestDTO);
}
