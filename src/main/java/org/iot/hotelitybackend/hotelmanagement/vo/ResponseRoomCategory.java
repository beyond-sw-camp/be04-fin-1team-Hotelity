package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseRoomCategory {
	public int roomCategoryCodePk;
	public String roomName;
	public int roomSubRoomsCount;
	public int roomCapacity;
	public int roomPrice;
	public String  roomSpecificInfo;
	public int roomBathroomCount;
	public String roomBedSize;
	public int roomBedCount;
	public int roomLevelCodeFk;
}
