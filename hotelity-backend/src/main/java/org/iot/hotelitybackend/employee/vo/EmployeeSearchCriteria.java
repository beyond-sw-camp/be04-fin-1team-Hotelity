package org.iot.hotelitybackend.employee.vo;

import lombok.Data;

@Data
public class EmployeeSearchCriteria {
	private Integer pageNum;
	private Integer employeeCode;
	private String employeeName;
	private String employeeAddress;
	private String employeePhoneNumber;
	private String employeeOfficePhoneNumber;
	private String employeeEmail;
	private String employeeResignStatus;
	private Integer permissionCodeFk;
	private Integer positionCodeFk;
	private Integer rankCodeFk;
	private Integer departmentCodeFk;
	private String branchCodeFk;
	private String orderBy;
	private Integer sortBy;
}
