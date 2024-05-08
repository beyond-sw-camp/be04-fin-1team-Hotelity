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
@Table(name = "department_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DepartmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "department_code_pk")
	private Integer departmentCodePk;
	private String departmentName;
	private Integer branchCodeFk;

	@Builder
	public DepartmentEntity(Integer departmentCodePk, String departmentName, Integer branchCodeFk) {
		this.departmentCodePk = departmentCodePk;
		this.departmentName = departmentName;
		this.branchCodeFk = branchCodeFk;
	}
}
