package org.iot.hotelitybackend.hotelservice.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RequestRegistStay {

	private Integer reservationCodePk;
	private Date reservationDate;
	private Date reservationCheckinDate;
	private Date reservationCheckoutDate;
	private Integer customerCodeFk;
	private String roomCodeFk;
	private Integer branchCodeFk;
	private Integer reservationCancelStatus;
	private Integer reservationPersonnel;
}
