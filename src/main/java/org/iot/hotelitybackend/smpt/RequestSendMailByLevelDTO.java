package org.iot.hotelitybackend.smpt;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RequestSendMailByLevelDTO {
	private String membershipLevelName;
	private LocalDateTime mailSendDate;
	private String sendType;
	private String title;
	private String messageContent;
	private int employeeCode;
	private int templateCode;
	private int reservationFk;
}
