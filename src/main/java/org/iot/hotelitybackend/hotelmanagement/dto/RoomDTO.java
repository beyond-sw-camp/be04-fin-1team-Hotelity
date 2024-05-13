package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class RoomDTO {
	private String roomCodePk;
	private String branchCodeFk;
	private int room_number;
	private int roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private float roomDiscountRate;
	private String roomImageLink;
}
