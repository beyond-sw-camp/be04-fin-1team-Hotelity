package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class RequestRegistBranch {
	private String branchCodePk;
	private String branchName;
	private String branchAddress;
	private String branchPhoneNumber;
}
