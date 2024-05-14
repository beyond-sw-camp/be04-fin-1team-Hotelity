package org.iot.hotelitybackend.hotelmanagement.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AncillaryDTO {
	private int ancillaryCodePk;
	private String ancillaryName;
	private String branchCodeFk;
	private String ancillaryLocation;
	private LocalTime ancillaryOpenTime;
	private LocalTime ancillaryCloseTime;
	private String ancillaryPhoneNumber;
	private int ancillaryCategoryCodeFk;
	private String branchName;
	private String ancillaryCategoryName;
}
