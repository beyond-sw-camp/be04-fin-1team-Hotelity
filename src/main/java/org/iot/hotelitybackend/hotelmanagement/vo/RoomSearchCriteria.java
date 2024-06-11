package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class RoomSearchCriteria {
	private Integer pageNum;
	private String roomCodePk;
	private String branchCodeFk;
	private Integer roomNumber;
	private String roomName;
	private String roomCurrentStatus;
	private Float roomDiscountRate;
	private String roomView;
	private Integer roomSubRoomsCount;
	private Integer minPrice;
	private Integer maxPrice;
	private Integer roomPrice;
	private Integer roomCapacity;
	private Integer roomBathroomCount;
	private String roomSpecificInfo;
	private String roomLevelName;
	private String orderBy;
	private Integer sortBy;
}
