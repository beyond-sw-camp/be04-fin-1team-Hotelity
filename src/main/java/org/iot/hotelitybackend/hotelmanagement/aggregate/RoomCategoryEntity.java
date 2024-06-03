package org.iot.hotelitybackend.hotelmanagement.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_category_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_category_code_pk")
	private Integer roomCategoryCodePk;
	private String roomName;
	private Integer roomSubRoomsCount;
	private Integer roomCapacity;
	private Integer roomPrice;
	private String  roomSpecificInfo;
	private Integer roomBathroomCount;
	private String roomBedSize;
	private Integer roomBedCount;
	private Integer roomLevelCodeFk;

	// @ManyToOne
	// @JoinColumn(name = "room_level_code_fk", insertable = false, updatable = false)
	// private RoomLevelEntity roomLevel;

	@Builder
	public RoomCategoryEntity(
		Integer roomCategoryCodePk,
		String roomName,
		Integer roomSubRoomsCount,
		Integer roomCapacity,
		Integer roomPrice,
		String roomSpecificInfo,
		Integer roomBathroomCount,
		String roomBedSize,
		Integer roomBedCount,
		Integer roomLevelCodeFk
	) {
		this.roomCategoryCodePk = roomCategoryCodePk;
		this.roomName = roomName;
		this.roomSubRoomsCount = roomSubRoomsCount;
		this.roomCapacity = roomCapacity;
		this.roomPrice = roomPrice;
		this.roomSpecificInfo = roomSpecificInfo;
		this.roomBathroomCount = roomBathroomCount;
		this.roomBedSize = roomBedSize;
		this.roomBedCount = roomBedCount;
		this.roomLevelCodeFk = roomLevelCodeFk;
	}
}
