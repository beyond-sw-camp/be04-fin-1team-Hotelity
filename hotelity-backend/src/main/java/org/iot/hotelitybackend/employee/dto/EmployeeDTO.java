package org.iot.hotelitybackend.employee.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private int employeeCodePk;
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

    private String nameOfPermission;
    private String nameOfPosition;
	private String nameOfRank;
	private String nameOfDepartment;
	private String nameOfBranch;
}
