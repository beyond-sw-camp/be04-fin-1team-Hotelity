package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class RoomDTO {
	public String roomCodePk;
	public int branchCodeFk;
	public int room_number;
	public int roomCategoryCodeFk;
	public String  roomCurrentStatus;
	public float roomDiscountRate;
	public String roomImageLink;
}
