package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StayDTO {
	private Integer stayCodePk;
	private Integer customerCodeFk;
	private String customerName;
	private String roomCodeFk;
	private Integer roomNumber;
	private String roomName;
	private String roomLevelName;
	private Integer roomCapacity;
	private Integer stayPeopleCount;
	private LocalDateTime stayCheckinTime;
	private LocalDateTime stayCheckoutTime;
	private LocalDateTime reservationCheckoutDate;
	private Integer employeeCodeFk;
	private String PICEmployeeName;
	private String branchCodeFk;
	private Integer reservationCodeFk;
	private String stayPeriod;
}
