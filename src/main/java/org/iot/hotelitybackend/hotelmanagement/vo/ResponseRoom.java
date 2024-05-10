package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseRoom {
	private String roomCodePk;
	private String branchCodeFk;
	private int roomNumber;
	private int roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private float roomDiscountRate;
	private String roomImageLink;
}
