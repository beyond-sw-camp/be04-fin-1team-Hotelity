package org.iot.hotelitybackend.customer.dto;

import java.util.Date;
import java.util.List;

import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.sales.dto.CouponIssueDTO;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
import org.iot.hotelitybackend.sales.dto.VocDTO;

import lombok.Data;

@Data
public class SelectCustomerDTO {
	private Integer customerCodePk;
	private String customerName;
	private String customerEmail;
	private String customerPhoneNumber;
	private String customerEnglishName;
	private String customerAddress;
	private Integer customerInfoAgreement;
	private Integer customerStatus;
	private Date customerRegisteredDate;
	private String customerType;
	private Integer nationCodeFk;
	private String customerGender;
	private String nationName;
	private String membershipLevelName;
	private List<PaymentDTO> payment;
	private List<VocDTO> voc;
	private List<StayDTO> stay;
	private List<CouponIssueDTO> couponIssue;
}
