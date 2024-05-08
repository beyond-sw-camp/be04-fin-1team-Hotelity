package org.iot.hotelitybackend.customer.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerDTO {
	private int customerCodePk;
	private String customerName;
	private String customerEmail;
	private String customerPhoneNumber;
	private String customerEnglishName;
	private String customerAddress;
	private int customerInfoAgreement;
	private int customerStatus;
	private Date customerRegisteredDate;
	private String customerType;
	private int nationCodeFk;
	private String customerGender;

}
