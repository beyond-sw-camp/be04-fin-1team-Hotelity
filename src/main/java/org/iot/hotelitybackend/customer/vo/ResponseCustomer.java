package org.iot.hotelitybackend.customer.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;

@Data
public class ResponseCustomer {
	public int customerCodePk;
	public String customerName;
	public String customerEmail;
	public String customerPhoneNumber;
	public String customerEnglishName;
	public String customerAddress;
	public int customerInfoAgreement;
	public int customerStatus;
	public Date customerRegisteredDate;
	public String customerType;
	public int nationCodeFk;
	public String customerGender;
	public String membershipLevelName;
	public List<MembershipDTO> memberships;
}
