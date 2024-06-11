package org.iot.hotelitybackend.employee.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;

@Entity
@Table(name = "employee_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_code_pk")
	private Integer employeeCodePk;
	private String employeeName;
	private String employeeAddress;
	private String employeePhoneNumber;
	private String employeeOfficePhoneNumber;
	private String employeeEmail;
	private String employeeSystemPassword;
	private String employeeResignStatus;
	@Column(name = "permission_code_fk", insertable=false, updatable=false)
	private Integer permissionCodeFk;
	@Column(name = "position_code_fk", insertable=false, updatable=false)
	private Integer positionCodeFk;
	@Column(name = "rank_code_fk", insertable=false, updatable=false)
	private Integer rankCodeFk;
	@Column(name = "department_code_fk", insertable=false, updatable=false)
	private Integer departmentCodeFk;
	@Column(name = "branch_code_fk", insertable=false, updatable=false)
	private String branchCodeFk;

	@Setter
	private String employeeProfileImageLink;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "permission_code_fk", referencedColumnName = "permission_code_pk", nullable = false)
	private PermissionEntity permission;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "position_code_fk", referencedColumnName = "position_code_pk", nullable = false)
	private PositionEntity position;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "rank_code_fk", referencedColumnName = "rank_code_pk", nullable = false)
	private RankEntity rank;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "department_code_fk", referencedColumnName = "department_code_pk", nullable = false)
	private DepartmentEntity department;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "branch_code_fk", referencedColumnName = "branch_code_pk", nullable = false)
	private BranchEntity branch;

	@Builder
	public EmployeeEntity(
		Integer employeeCodePk,
		String employeeName,
		String employeeAddress,
		String employeePhoneNumber,
		String employeeOfficePhoneNumber,
		String employeeEmail,
		String employeeSystemPassword,
		String employeeResignStatus,
		Integer permissionCodeFk,
		Integer positionCodeFk,
		Integer rankCodeFk,
		Integer departmentCodeFk,
		String branchCodeFk,
		String employeeProfileImageLink,
		PermissionEntity employeePermission,
		PositionEntity employeePosition,
		RankEntity employeeRank,
		DepartmentEntity employeeDepartment,
		BranchEntity employeeBranch
    ) {
		this.employeeCodePk = employeeCodePk;
		this.employeeName = employeeName;
		this.employeeAddress = employeeAddress;
		this.employeePhoneNumber = employeePhoneNumber;
		this.employeeOfficePhoneNumber = employeeOfficePhoneNumber;
		this.employeeEmail = employeeEmail;
		this.employeeSystemPassword = employeeSystemPassword;
		this.employeeResignStatus = employeeResignStatus;
		this.permissionCodeFk = permissionCodeFk;
		this.positionCodeFk = positionCodeFk;
		this.rankCodeFk = rankCodeFk;
		this.departmentCodeFk = departmentCodeFk;
		this.branchCodeFk = branchCodeFk;
		this.employeeProfileImageLink = employeeProfileImageLink;

		this.permission = employeePermission;
		this.position = employeePosition;
		this.rank = employeeRank;
		this.department = employeeDepartment;
		this.branch = employeeBranch;
    }

	public String getPermissionName() {
		if (permission != null) {
			return permission.getPermissionName();
		}
		return "";
	}

	public int getPermissionId() {
		if (permission != null) {
			return permission.getPermissionCodePk();
		}
		return -1;
	}

	public String getPositionName() {
		if (position != null) {
			return position.getPositionName();
		}
		return "";
	}

	public int getPositionId() {
		if (position != null) {
			return position.getPositionCodePk();
		}
		return -1;
	}

	public String getRankName() {
		if (rank != null) {
			return rank.getRankName();
		}
		return "";
	}

	public int getRankId() {
		if (rank != null) {
			return rank.getRankCodePk();
		}
		return -1;
	}

	public String getDepartmentName() {
		if (department != null) {
			return department.getDepartmentName();
		}
		return "";
	}

	public int getDepartmentId() {
		if (department != null) {
			return department.getDepartmentCodePk();
		}
		return -1;
	}

	public String getBranchName() {
		if (branch != null) {
			return branch.getBranchName();
		}
		return "";
	}

	public String getBranchId() {
		if (branch != null) {
			return branch.getBranchCodePk();
		}
		return "";
	}
}
