package org.iot.hotelitybackend.smpt;

import lombok.Data;

@Data
public class RequestDTO {
	private String address;
	private String title;
	private String message;
	private int employeeFk;
	private int templateFk;
	private int reservationFk;
}
