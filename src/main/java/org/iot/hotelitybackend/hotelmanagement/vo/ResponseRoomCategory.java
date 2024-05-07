package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseRoomCategory {
	private int roomCategoryCodePk;
	private String roomName;
	private int roomSubRoomsCount;
	private int roomCapacity;
	private int roomPrice;
	private String  roomSpecificInfo;
	private int roomBathroomCount;
	private String roomBedSize;
	private int roomBedCount;
	private int roomLevelCodeFk;
}
