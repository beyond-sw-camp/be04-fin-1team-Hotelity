package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class RoomDTO {
	private String roomCodePk;
	private int branchCodeFk;
	private int room_number;
	private int roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private float roomDiscountRate;
	private String roomImageLink;
}
