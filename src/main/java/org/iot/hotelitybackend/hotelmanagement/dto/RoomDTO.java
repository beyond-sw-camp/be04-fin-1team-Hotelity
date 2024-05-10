package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class RoomDTO {
	private String roomCodePk;
	private String branchCodeFk;
	private int roomNumber;
	private int roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private float roomDiscountRate;
	private String roomImageLink;
	private String roomView;
	private RoomCategoryDTO roomCategoryDTO;
}
