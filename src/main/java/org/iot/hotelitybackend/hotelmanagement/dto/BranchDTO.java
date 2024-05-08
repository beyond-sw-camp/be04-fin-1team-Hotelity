package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class BranchDTO {
	private int branchCodePk;
	private String branchName;
	private String branchAddress;
	private String branchPhoneNumber;
}
