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
@Table(name = "room_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_code_pk")
	public String roomCodePk;
	public Integer branchCodeFk;
	public Integer room_number;
	public Integer roomCategoryCodeFk;
	public String  roomCurrentStatus;
	public Float roomDiscountRate;
	public String roomImageLink;
}
