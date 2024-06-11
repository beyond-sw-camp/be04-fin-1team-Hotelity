package org.iot.hotelitybackend.hotelservice.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StaySearchCriteria {
	private Integer pageNum;
	private Integer stayCodePk;
	private Integer customerCodeFk;
	private String customerName;
	private String roomCodeFk;
	private String roomName;
	private String roomLevelName;
	private Integer roomCapacity;
	private Integer stayPeopleCount;
	private LocalDateTime stayCheckinTime;
	private LocalDateTime reservationCheckoutDate;
	private LocalDateTime stayCheckoutTime;
	private String branchCodeFk;
	private Integer employeeCodeFk;
	private String employeeName;
	private Integer reservationCodeFk;
	private Integer stayCheckoutStatus;
	private Integer stayPeriod;
	private String orderBy;
	private Integer sortBy;

	public StaySearchCriteria(Integer pageNum, Integer stayCodePk, Integer customerCodeFk, String customerName,
		String roomCodeFk, String roomName, String roomLevelName, Integer roomCapacity, Integer stayPeopleCount,
		LocalDateTime stayCheckinTime, LocalDateTime reservationCheckoutDate, LocalDateTime stayCheckoutTime,
		String branchCodeFk, Integer employeeCodeFk, String employeeName, Integer reservationCodeFk,
		Integer stayCheckoutStatus, Integer stayPeriod, String orderBy, Integer sortBy) {
		this.pageNum = pageNum;
		this.stayCodePk = stayCodePk;
		this.customerCodeFk = customerCodeFk;
		this.customerName = customerName;
		this.roomCodeFk = roomCodeFk;
		this.roomName = roomName;
		this.roomLevelName = roomLevelName;
		this.roomCapacity = roomCapacity;
		this.stayPeopleCount = stayPeopleCount;
		this.stayCheckinTime = stayCheckinTime;
		this.reservationCheckoutDate = reservationCheckoutDate;
		this.stayCheckoutTime = stayCheckoutTime;
		this.branchCodeFk = branchCodeFk;
		this.employeeCodeFk = employeeCodeFk;
		this.employeeName = employeeName;
		this.reservationCodeFk = reservationCodeFk;
		this.stayCheckoutStatus = stayCheckoutStatus;
		this.stayPeriod = stayPeriod;
		this.orderBy = orderBy;
		this.sortBy = sortBy;
	}
}
