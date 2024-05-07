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
@Table(name = "permission_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PermissionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "permission_code_pk")
	private Integer permissionCodePk;
	private String permissionName;
	private Integer departmentCodeFk;

	@Builder
	public PermissionEntity(Integer permissionCodePk, String permissionName, Integer departmentCodeFk) {
		this.permissionCodePk = permissionCodePk;
		this.permissionName = permissionName;
		this.departmentCodeFk = departmentCodeFk;
	}
}
