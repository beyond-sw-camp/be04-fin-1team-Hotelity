package org.iot.hotelitybackend.hotelmanagement.aggregate;

import java.util.Date;

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
@Table(name = "room_maintenance_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomMaintenanceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_maintenance_code_pk")
	public Integer roomMaintenanceCodePk;
	public Date roomMaintenanceDate;
	public String roomMaintenanceStatus;
	public Integer roomCodeFk;
}
