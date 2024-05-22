package org.iot.hotelitybackend.smpt;

import lombok.Data;

@Data
public class mailDTO {
	private String address;
	private String title;
	private String message;
}
