package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseRoom {
	private String roomCodePk;
	private int branchCodeFk;
	private int room_number;
	private int roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private float roomDiscountRate;
	private String roomImageLink;
}
