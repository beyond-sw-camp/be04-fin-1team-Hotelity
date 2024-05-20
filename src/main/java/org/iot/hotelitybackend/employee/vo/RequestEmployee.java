package org.iot.hotelitybackend.employee.vo;

import lombok.Data;

@Data
public class RequestEmployee {
	private String employeeName;
	private String employeeAddress;
	private String employeePhoneNumber;
	private String employeeOfficePhoneNumber;
	private String employeeEmail;
	private String employeeSystemPassword;
	private String employeeResignStatus;
	private String employeeProfileImageLink;

	private int permissionCodeFk;
	private int positionCodeFk;
	private int rankCodeFk;
	private int departmentCodeFk;
	private String branchCodeFk;
}
