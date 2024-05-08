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
@Table(name = "room_level_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomLevelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_level_code_pk")
	private Integer roomLevelCodePk;
	private String roomLevelName;

	@Builder
	public RoomLevelEntity(Integer roomLevelCodePk, String roomLevelName) {
		this.roomLevelCodePk = roomLevelCodePk;
		this.roomLevelName = roomLevelName;
	}
}
