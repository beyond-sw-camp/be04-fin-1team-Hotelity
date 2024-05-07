package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

@Data
public class ResponseBranch {
	public int branchCodePk;
	public String branchName;
	public String branchAddress;
	public String branchPhoneNumber;
}
