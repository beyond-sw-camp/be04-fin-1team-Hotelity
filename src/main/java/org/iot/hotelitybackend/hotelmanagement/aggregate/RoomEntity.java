package org.iot.hotelitybackend.hotelmanagement.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_code_pk")
	private String roomCodePk;
	private String branchCodeFk;
	private Integer room_number;
	private Integer roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private Float roomDiscountRate;
	private String roomImageLink;

	@Builder
	public RoomEntity(String roomCodePk, String branchCodeFk, Integer room_number, Integer roomCategoryCodeFk,
		String roomCurrentStatus, Float roomDiscountRate, String roomImageLink) {
		this.roomCodePk = roomCodePk;
		this.branchCodeFk = branchCodeFk;
		this.room_number = room_number;
		this.roomCategoryCodeFk = roomCategoryCodeFk;
		this.roomCurrentStatus = roomCurrentStatus;
		this.roomDiscountRate = roomDiscountRate;
		this.roomImageLink = roomImageLink;
	}
}
