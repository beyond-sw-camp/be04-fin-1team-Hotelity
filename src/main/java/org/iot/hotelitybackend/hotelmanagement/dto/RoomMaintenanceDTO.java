package org.iot.hotelitybackend.hotelmanagement.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RoomMaintenanceDTO {
	public int roomMaintenanceCodePk;
	public Date roomMaintenanceDate;
	public String roomMaintenanceStatus;
	public int roomCodeFk;
}
