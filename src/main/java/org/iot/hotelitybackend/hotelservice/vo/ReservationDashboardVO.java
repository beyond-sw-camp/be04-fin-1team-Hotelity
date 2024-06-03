package org.iot.hotelitybackend.hotelservice.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationDashboardVO {
	private Integer reservationCodePk;
	private Integer customerCodeFk;
	private String customerName;
	private String roomCodeFk;
	private String roomName;
	private String roomLevelName;
	private LocalDateTime reservationDate;
}
