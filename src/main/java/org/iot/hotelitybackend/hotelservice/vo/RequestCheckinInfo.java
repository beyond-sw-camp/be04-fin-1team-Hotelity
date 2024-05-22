package org.iot.hotelitybackend.hotelservice.vo;

import lombok.Data;

@Data
public class RequestCheckinInfo {
	private Integer reservationCodeFk;
	private Integer employeeCodeFk;
	private Integer stayPeopleCount;
	private Integer reservationCheckinStatus;
}
