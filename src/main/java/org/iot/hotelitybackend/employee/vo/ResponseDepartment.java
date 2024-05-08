package org.iot.hotelitybackend.employee.vo;

import lombok.Data;

@Data
public class ResponseDepartment {
	private int departmentCodePk;
	private String departmentName;
	private int branchCodeFk;
}
