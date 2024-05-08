package org.iot.hotelitybackend.employee.dto;

import lombok.Data;

@Data
public class DepartmentDTO {
	private int departmentCodePk;
	private String departmentName;
	private int branchCodeFk;
}
