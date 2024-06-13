package org.iot.hotelitybackend.smpt;

import org.iot.hotelitybackend.sales.aggregate.VocEntity;

import java.util.List;
import java.util.Map;

public interface EmailService {
	Map<String, Object> mailsend(List<RequestDTO> requestDTO);

	Map<String, Object> mailsendByMembershipLevel(RequestSendMailByLevelDTO requestSendMailByLevelDTO);

	void sendVocProcessedEmail(VocEntity vocEntity);
}
