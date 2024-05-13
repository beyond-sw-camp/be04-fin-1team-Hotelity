package org.iot.hotelitybackend.employee.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
	private int employeeCodePk;
	private String branchCodeFk;
	private int departmentCodeFk;
	private String employeeName;
	private String employeeAddress;
	private String employeePhoneNumber;
	private String employeeOfficePhoneNumber;
	private String employeeEmail;
	private int positionCodeFk;
	private int rankCodeFk;
	private String employeeSystemPassword;
	private String employeeResignStatus;
	private int permissionCodeFk;
	private String employeeProfileImageLink;
}
