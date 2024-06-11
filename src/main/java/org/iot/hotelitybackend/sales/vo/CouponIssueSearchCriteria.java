package org.iot.hotelitybackend.sales.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CouponIssueSearchCriteria {
	private Integer pageNum;
	private Integer couponIssueCodePk;
	private String couponName;
	private String customerName;
	private Integer customerCodePk;
	private Double couponDiscountRate;
	private LocalDateTime couponIssueDate;
	private LocalDateTime couponExpireDate;
	private LocalDateTime couponUseDate;
	private String orderBy;
	private Integer sortBy;

	public CouponIssueSearchCriteria(Integer pageNum, Integer couponIssueCodePk, String couponName, String customerName,
		Integer customerCodePk, Double couponDiscountRate, LocalDateTime couponIssueDate,
		LocalDateTime couponExpireDate,
		LocalDateTime couponUseDate, String orderBy, Integer sortBy) {
		this.pageNum = pageNum;
		this.couponIssueCodePk = couponIssueCodePk;
		this.couponName = couponName;
		this.customerName = customerName;
		this.customerCodePk = customerCodePk;
		this.couponDiscountRate = couponDiscountRate;
		this.couponIssueDate = couponIssueDate;
		this.couponExpireDate = couponExpireDate;
		this.couponUseDate = couponUseDate;
		this.orderBy = orderBy;
		this.sortBy = sortBy;
	}
}
