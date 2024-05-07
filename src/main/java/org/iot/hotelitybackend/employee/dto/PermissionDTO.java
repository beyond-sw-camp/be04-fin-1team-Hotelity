package org.iot.hotelitybackend.employee.dto;

import lombok.Data;

@Data
public class PermissionDTO {
	private int permissionCodePk;
	private String permissionName;
	private int departmentCodeFk;
}
