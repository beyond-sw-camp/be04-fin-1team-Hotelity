package org.iot.hotelitybackend.hotelmanagement.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RequestModifyCustomer {
	private String customerName;
	private String customerEmail;
	private String customerPhoneNumber;
	private String customerEnglishName;
	private String customerAddress;
	private int customerStatus;
	private String customerType;
	private int nationCodeFk;
	private String customerGender;
	private int membershipLevelCode;
}
