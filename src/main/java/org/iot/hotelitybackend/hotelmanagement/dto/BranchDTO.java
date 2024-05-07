package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class BranchDTO {
	public int branchCodePk;
	public String branchName;
	public String branchAddress;
	public String branchPhoneNumber;
}
