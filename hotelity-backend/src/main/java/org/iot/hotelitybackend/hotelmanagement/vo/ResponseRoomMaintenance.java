package org.iot.hotelitybackend.hotelmanagement.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseRoomMaintenance {
	private int roomMaintenanceCodePk;
	private Date roomMaintenanceDate;
	private String roomMaintenanceStatus;
	private int roomCodeFk;
}
