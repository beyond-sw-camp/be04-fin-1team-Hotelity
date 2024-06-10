package org.iot.hotelitybackend.sales.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VocSearchCriteria {
	private Integer pageNum;
	private Integer vocCodePk;
	private String vocTitle;
	private String vocCategory;
	private Integer customerCodeFk;
	private String customerName;
	private LocalDateTime vocCreatedDate;
	private LocalDateTime vocLastUpdatedDate;
	private String branchCodeFk;
	private Integer employeeCodeFk;
	private String employeeName;
	private Integer vocProcessStatus;

	public VocSearchCriteria(Integer pageNum, Integer vocCodePk, String vocTitle, String vocCategory,
		Integer customerCodeFk, String customerName, LocalDateTime vocCreatedDate, LocalDateTime vocLastUpdatedDate,
		String branchCodeFk, Integer employeeCodeFk, String employeeName, Integer vocProcessStatus, String orderBy,
		Integer sortBy) {
		this.pageNum = pageNum;
		this.vocCodePk = vocCodePk;
		this.vocTitle = vocTitle;
		this.vocCategory = vocCategory;
		this.customerCodeFk = customerCodeFk;
		this.customerName = customerName;
		this.vocCreatedDate = vocCreatedDate;
		this.vocLastUpdatedDate = vocLastUpdatedDate;
		this.branchCodeFk = branchCodeFk;
		this.employeeCodeFk = employeeCodeFk;
		this.employeeName = employeeName;
		this.vocProcessStatus = vocProcessStatus;
		this.orderBy = orderBy;
		this.sortBy = sortBy;
	}

	private String orderBy;
	private Integer sortBy;
}
