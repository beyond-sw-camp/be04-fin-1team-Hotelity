package org.iot.hotelitybackend.employee.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;

import java.util.List;

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
	private String employeeProfileImageLink;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "permission_code_fk", nullable = false)
	private PermissionEntity permission;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "position_code_fk", nullable = false)
	private PositionEntity position;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "rank_code_fk", nullable = false)
	private RankEntity rank;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "department_code_fk", nullable = false)
	private DepartmentEntity department;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "branch_code_fk", nullable = false)
	private BranchEntity branch;

	@JoinColumn(name="employee_code_fk")
	@OneToMany
	private List<StayEntity> stayList;

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
            String employeeProfileImageLink,

			PermissionEntity employPermission,
			PositionEntity employPosition,
			RankEntity employRank,
			DepartmentEntity employDepartment,
			BranchEntity employBranch,

			List<StayEntity> stayList
    ) {
		this.employeeCodePk = employeeCodePk;
		this.employeeName = employeeName;
		this.employeeAddress = employeeAddress;
		this.employeePhoneNumber = employeePhoneNumber;
		this.employeeOfficePhoneNumber = employeeOfficePhoneNumber;
		this.employeeEmail = employeeEmail;
		this.employeeSystemPassword = employeeSystemPassword;
		this.employeeResignStatus = employeeResignStatus;
		this.employeeProfileImageLink = employeeProfileImageLink;

		this.permission = employPermission;
		this.position = employPosition;
		this.rank = employRank;
		this.department = employDepartment;
		this.branch = employBranch;

        this.stayList = stayList;
    }

	public String getPermissionName() {
		if (permission != null) {
			return permission.getPermissionName();
		}
		return "";
	}

	public String getPositionName() {
		if (position != null) {
			return position.getPositionName();
		}
		return "";
	}

	public String getRankName() {
		if (rank != null) {
			return rank.getRankName();
		}
		return "";
	}

	public String getDepartmentName() {
		if (department != null) {
			return department.getDepartmentName();
		}
		return "";
	}

	public String getBranchName() {
		if (branch != null) {
			return branch.getBranchName();
		}
		return "";
	}

}
