package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;

@Entity
@Table(name = "voc_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VocEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vocCodePk;
	private String vocContent;
	private LocalDateTime vocCreatedDate;
	private LocalDateTime vocLastUpdatedDate;

	@Column(name = "customer_code_fk")
	private Integer customerCodeFk;

	private Integer vocProcessStatus;
	private String vocCategory;
	private String vocTitle;

	@Column(name = "employee_code_fk")
	private Integer employeeCodeFk;

	@Column(name = "branch_code_fk")
	private String branchCodeFk;
	private String vocImageLink;
	private String vocResponse;

	@Builder
	public VocEntity(
		Integer vocCodePk,
		String vocContent,
		LocalDateTime vocCreatedDate,
		LocalDateTime vocLastUpdatedDate,
		Integer customerCodeFk,
		Integer vocProcessStatus,
		String vocCategory,
		String vocTitle,
		Integer employeeCodeFk,
		String branchCodeFk,
		String vocImageLink,
		String vocResponse
	) {
		this.vocCodePk = vocCodePk;
		this.vocContent = vocContent;
		this.vocCreatedDate = vocCreatedDate;
		this.vocLastUpdatedDate = vocLastUpdatedDate;
		this.customerCodeFk = customerCodeFk;
		this.vocProcessStatus = vocProcessStatus;
		this.vocCategory = vocCategory;
		this.vocTitle = vocTitle;
		this.employeeCodeFk = employeeCodeFk;
		this.branchCodeFk = branchCodeFk;
		this.vocImageLink = vocImageLink;
		this.vocResponse = vocResponse;
	}

	@ManyToOne
	@JoinColumn(name = "customer_code_fk", insertable = false, updatable = false)
	private CustomerEntity customer;

	@ManyToOne
	@JoinColumn(name = "employee_code_fk", insertable = false, updatable = false)
	private EmployeeEntity employee;

	@ManyToOne
	@JoinColumn(name = "branch_code_fk", insertable = false, updatable = false)
	private BranchEntity branch;
}
