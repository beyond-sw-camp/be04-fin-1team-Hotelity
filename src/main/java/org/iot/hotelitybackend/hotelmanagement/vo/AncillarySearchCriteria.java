package org.iot.hotelitybackend.hotelmanagement.vo;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AncillarySearchCriteria {
	private Integer pageNum;
	private Integer ancillaryCodePk;
	private String ancillaryName;
	private String branchCodeFk;
	private String ancillaryLocation;
	private LocalTime ancillaryOpenTime;
	private LocalTime ancillaryCloseTime;
	private String ancillaryPhoneNumber;
	private Integer ancillaryCategoryCodeFk;
	private String branchName;
	private String ancillaryCategoryName;
	private String orderBy;
	private Integer sortBy;
}
