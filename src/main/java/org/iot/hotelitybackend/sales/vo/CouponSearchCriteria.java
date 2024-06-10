package org.iot.hotelitybackend.sales.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CouponSearchCriteria {
	private Integer pageNum;
	private Integer couponCodePk;
	private String couponName;
	private String couponType;
	private Double couponDiscountRate;
	private Date couponLaunchingDate;
	private String couponInfo;
	private Integer membershipLevelCodeFk;
	private String orderBy;
	private Integer sortBy;
}
