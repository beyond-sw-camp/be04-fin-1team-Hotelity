package org.iot.hotelitybackend.hotelmanagement.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AncillaryDTO {
	public int ancillaryCodePk;
	public String ancillaryName;
	public int branchCodeFk;
	public String ancillaryLocation;
	public LocalTime ancillaryOpenTime;
	public LocalTime ancillaryCloseTime;
	public String ancillaryPoneNumber;
	public int ancillaryCategoryCodeFk;
}
