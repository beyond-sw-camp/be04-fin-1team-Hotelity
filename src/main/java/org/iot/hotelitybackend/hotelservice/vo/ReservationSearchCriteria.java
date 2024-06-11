package org.iot.hotelitybackend.hotelservice.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationSearchCriteria {
	private Integer reservationCodePk;
	private Integer customerCodeFk;
	private String customerName;
	private String customerEnglishName;
	private String roomCodeFk;
	private String roomName;
	private String roomLevelName;
	private Integer roomCapacity;
	private String branchCodeFk;
	private LocalDateTime reservationDate;
	private LocalDateTime reservationCheckinDate;
	private LocalDateTime reservationCheckoutDate;
	private Integer reservationCancelStatus;
	private String orderBy;
	private Integer sortBy;
}
