package org.iot.hotelitybackend.customer.aggregate;

import java.util.Date;
import java.util.Set;

import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_tb")
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CustomerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_code_pk")
	public Integer customerCodePk;
	public String customerName;
	public String customerEmail;
	public String customerPhoneNumber;
	public String customerEnglishName;
	public String customerAddress;
	public Integer customerInfoAgreement;
	public Integer customerStatus;
	public Date customerRegisteredDate;
	public String customerType;
	public Integer nationCodeFk;
	public String customerGender;

	@Builder
	public CustomerEntity(
		Integer customerCodePk,
		String customerName,
		String customerEmail,
		String customerPhoneNumber,
		String customerEnglishName,
		String customerAddress,
		Integer customerInfoAgreement,
		Integer customerStatus,
		Date customerRegisteredDate,
		String customerType,
		Integer nationCodeFk,
		String customerGender
	) {
		this.customerCodePk = customerCodePk;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerEnglishName = customerEnglishName;
		this.customerAddress = customerAddress;
		this.customerInfoAgreement = customerInfoAgreement;
		this.customerStatus = customerStatus;
		this.customerRegisteredDate = customerRegisteredDate;
		this.customerType = customerType;
		this.nationCodeFk = nationCodeFk;
		this.customerGender = customerGender;
	}

	@OneToMany(mappedBy = "customer")
	private Set<MembershipIssueEntity> membershipIssues;

	public CustomerEntity() {

	}
}
