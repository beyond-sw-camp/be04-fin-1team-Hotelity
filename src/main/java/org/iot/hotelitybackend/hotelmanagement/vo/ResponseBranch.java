package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseBranch {
	private int branchCodePk;
	private String branchName;
	private String branchAddress;
	private String branchPhoneNumber;
}
