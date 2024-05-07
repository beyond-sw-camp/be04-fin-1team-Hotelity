package org.iot.hotelitybackend.hotelmanagement.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseRoomMaintenance {
	public int roomMaintenanceCodePk;
	public Date roomMaintenanceDate;
	public String roomMaintenanceStatus;
	public int roomCodeFk;
}
