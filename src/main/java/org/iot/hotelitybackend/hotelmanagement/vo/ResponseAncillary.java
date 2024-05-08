package org.iot.hotelitybackend.hotelmanagement.vo;

import java.time.LocalTime;

import lombok.Data;

@Data
public class ResponseAncillary {
	private int ancillaryCodePk;
	private String ancillaryName;
	private int branchCodeFk;
	private String ancillaryLocation;
	private LocalTime ancillaryOpenTime;
	private LocalTime ancillaryCloseTime;
	private String ancillaryPhoneNumber;
	private int ancillaryCategoryCodeFk;
}
