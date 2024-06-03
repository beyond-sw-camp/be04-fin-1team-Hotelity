package org.iot.hotelitybackend.sales.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
public class VocDashboardVO {
	private Integer vocCodePk;
	private String vocTitle;
	private String vocCategory;
	private LocalDateTime vocCreatedDate;
	private String branchCodeFk;
	private Integer employeeCodeFk;
	private String PICEmployeeName;
	private Integer vocProcessStatus;

	// @Builder
	// public VocDashboardVO(Integer vocCodePk, String vocTitle, String vocCategory, LocalDateTime vocCreatedDate,
	// 	String branchCodeFk, Integer employeeCodeFk, String PICEmployeeName, Integer vocProcessStatus) {
	// 	this.vocCodePk = vocCodePk;
	// 	this.vocTitle = vocTitle;
	// 	this.vocCategory = vocCategory;
	// 	this.vocCreatedDate = vocCreatedDate;
	// 	this.branchCodeFk = branchCodeFk;
	// 	this.employeeCodeFk = employeeCodeFk;
	// 	this.PICEmployeeName = PICEmployeeName;
	// 	this.vocProcessStatus = vocProcessStatus;
	// }
}
