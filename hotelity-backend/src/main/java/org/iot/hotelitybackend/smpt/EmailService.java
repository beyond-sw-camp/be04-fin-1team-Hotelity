package org.iot.hotelitybackend.smpt;

import java.util.List;
import java.util.Map;

public interface EmailService {
	Map<String, Object> mailsend(List<RequestDTO> requestDTO);

	Map<String, Object> mailsendByMembershipLevel(RequestSendMailByLevelDTO requestSendMailByLevelDTO);
}
