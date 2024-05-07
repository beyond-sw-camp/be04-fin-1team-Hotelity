package org.iot.hotelitybackend.hotelmanagement.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
	public Integer roomCategoryCodePk;
	public String roomName;
	public Integer roomSubRoomsCount;
	public Integer roomCapacity;
	public Integer roomPrice;
	public String  roomSpecificInfo;
	public Integer roomBathroomCount;
	public String roomBedSize;
	public Integer roomBedCount;
	public Integer roomLevelCodeFk;
}
