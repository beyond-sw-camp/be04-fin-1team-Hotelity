package org.iot.hotelitybackend.employee.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_code_pk")
	private String employeeCodePk;
	private Integer branchCodeFk;
	private Integer departmentCodeFk;
	private String employeeName;
	private String employeeAddress;
	private String employeePhoneNumber;
	private String employeeOfficePhoneNumber;
	private String employeeEmail;
	private Integer positionCodeFk;
	private Integer rankCodeFk;
	private String employeeSystemPassword;
	private String employeeResignStatus;
	private Integer permissionCodeFk;
	private String employeeProfileImageLink;

	@Builder
	public EmployeeEntity(
		String employeeCodePk,
		Integer branchCodeFk,
		Integer departmentCodeFk,
		String employeeName,
		String employeeAddress,
		String employeePhoneNumber,
		String employeeOfficePhoneNumber,
		String employeeEmail,
		Integer positionCodeFk,
		Integer rankCodeFk,
		String employeeSystemPassword,
		String employeeResignStatus,
		Integer permissionCodeFk,
		String employeeProfileImageLink
	) {
		this.employeeCodePk = employeeCodePk;
		this.branchCodeFk = branchCodeFk;
		this.departmentCodeFk = departmentCodeFk;
		this.employeeName = employeeName;
		this.employeeAddress = employeeAddress;
		this.employeePhoneNumber = employeePhoneNumber;
		this.employeeOfficePhoneNumber = employeeOfficePhoneNumber;
		this.employeeEmail = employeeEmail;
		this.positionCodeFk = positionCodeFk;
		this.rankCodeFk = rankCodeFk;
		this.employeeSystemPassword = employeeSystemPassword;
		this.employeeResignStatus = employeeResignStatus;
		this.permissionCodeFk = permissionCodeFk;
		this.employeeProfileImageLink = employeeProfileImageLink;
	}
}
