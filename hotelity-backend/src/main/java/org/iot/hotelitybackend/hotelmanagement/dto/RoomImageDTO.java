package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class RoomImageDTO {
	private int roomImageCodePk;
	private String roomCodeFk;
	private String roomImageLink;
}
