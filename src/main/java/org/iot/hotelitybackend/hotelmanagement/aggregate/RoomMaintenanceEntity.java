package org.iot.hotelitybackend.hotelmanagement.aggregate;

import java.util.Date;

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
@Table(name = "room_maintenance_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomMaintenanceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_maintenance_code_pk")
	private Integer roomMaintenanceCodePk;
	private Date roomMaintenanceDate;
	private String roomMaintenanceStatus;
	private Integer roomCodeFk;

	@Builder
	public RoomMaintenanceEntity(Integer roomMaintenanceCodePk, Date roomMaintenanceDate, String roomMaintenanceStatus,
		Integer roomCodeFk) {
		this.roomMaintenanceCodePk = roomMaintenanceCodePk;
		this.roomMaintenanceDate = roomMaintenanceDate;
		this.roomMaintenanceStatus = roomMaintenanceStatus;
		this.roomCodeFk = roomCodeFk;
	}
}
