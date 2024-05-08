package org.iot.hotelitybackend.employee.vo;

import lombok.Data;

@Data
public class ResponsePermission {
	private int permissionCodePk;
	private String permissionName;
	private int departmentCodeFk;
}
