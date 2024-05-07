package org.iot.hotelitybackend.hotelmanagement.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RoomMaintenanceDTO {
	private int roomMaintenanceCodePk;
	private Date roomMaintenanceDate;
	private String roomMaintenanceStatus;
	private int roomCodeFk;
}
