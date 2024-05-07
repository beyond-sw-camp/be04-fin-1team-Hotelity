package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseRoom {
	public String roomCodePk;
	public int branchCodeFk;
	public int room_number;
	public int roomCategoryCodeFk;
	public String  roomCurrentStatus;
	public float roomDiscountRate;
	public String roomImageLink;
}
