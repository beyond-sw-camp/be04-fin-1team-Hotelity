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
	private Integer permissionCode;
	private Integer positionCode;
	private Integer rankCode;
	private Integer departmentCode;
	private String branchCode;
	private String orderBy;
	private Integer sortBy;
}
