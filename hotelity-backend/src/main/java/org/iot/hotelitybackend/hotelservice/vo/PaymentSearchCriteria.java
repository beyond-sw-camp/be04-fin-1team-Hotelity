package org.iot.hotelitybackend.hotelservice.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentSearchCriteria {
	private Integer pageNum;
	private Integer customerCodeFk;
	private String customerName;
	private LocalDateTime paymentDate;
	private Integer paymentCancelStatus;
	private String paymentMethod;
	private Integer reservationCodeFk;
	private Integer paymentTypeCodeFk;
	private String paymentTypeName;
	private String orderBy;
	private Integer sortBy;

	public PaymentSearchCriteria(Integer pageNum, Integer customerCodeFk, String customerName,
		LocalDateTime paymentDate,
		Integer paymentCancelStatus, String paymentMethod, Integer reservationCodeFk, Integer paymentTypeCodeFk,
		String paymentTypeName, String orderBy, Integer sortBy) {
		this.pageNum = pageNum;
		this.customerCodeFk = customerCodeFk;
		this.customerName = customerName;
		this.paymentDate = paymentDate;
		this.paymentCancelStatus = paymentCancelStatus;
		this.paymentMethod = paymentMethod;
		this.reservationCodeFk = reservationCodeFk;
		this.paymentTypeCodeFk = paymentTypeCodeFk;
		this.paymentTypeName = paymentTypeName;
		this.orderBy = orderBy;
		this.sortBy = sortBy;
	}
}
