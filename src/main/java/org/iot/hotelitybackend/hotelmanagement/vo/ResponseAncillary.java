package org.iot.hotelitybackend.hotelmanagement.vo;

import java.time.LocalTime;

import lombok.Data;

@Data
public class ResponseAncillary {
	public int ancillaryCodePk;
	public String ancillaryName;
	public int branchCodeFk;
	public String ancillaryLocation;
	public LocalTime ancillaryOpenTime;
	public LocalTime ancillaryCloseTime;
	public String ancillaryPhoneNumber;
	public int ancillaryCategoryCodeFk;
}
