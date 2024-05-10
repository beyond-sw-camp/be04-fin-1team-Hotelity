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
	private Integer branchCodeFk;
	private Integer roomNumber;
	private Integer roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private Float roomDiscountRate;
	private String roomImageLink;
	private String roomView;

	@Builder
	public RoomEntity(String roomCodePk, Integer branchCodeFk, Integer roomNumber, Integer roomCategoryCodeFk,
		String roomCurrentStatus, Float roomDiscountRate, String roomImageLink, String roomView) {
		this.roomCodePk = roomCodePk;
		this.branchCodeFk = branchCodeFk;
		this.roomNumber = roomNumber;
		this.roomCategoryCodeFk = roomCategoryCodeFk;
		this.roomCurrentStatus = roomCurrentStatus;
		this.roomDiscountRate = roomDiscountRate;
		this.roomImageLink = roomImageLink;
		this.roomView = roomView;
	}
}
