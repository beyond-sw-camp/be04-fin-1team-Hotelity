package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class RoomCategoryDTO {
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
