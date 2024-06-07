package org.iot.hotelitybackend.customer.aggregate;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.Formula;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@Column(name = "nation_code_fk")
	public Integer nationCodeFk;

	public String customerGender;

	@ManyToOne
	@JoinColumn(name = "nation_code_fk", insertable = false, updatable = false)
	private NationEntity nation;

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
		String customerGender,
		String membershipLevelName
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
		this.membershipLevelName = membershipLevelName;
	}

	@OneToMany(mappedBy = "customer")
	private List<MembershipIssueEntity> membershipIssues;



	@Formula(
		"(SELECT m.membership_level_name "
			+ "FROM membership_tb m "
			+ "JOIN membership_issue_tb mi ON m.membership_level_code_pk = mi.membership_level_code_fk "
			+ "WHERE mi.customer_code_fk = customer_code_pk)"
	)
	private String membershipLevelName;

	@Formula(
		"(SELECT n.nation_name "
			+ "FROM nationality_tb n "
			+ "WHERE n.nation_code_pk = nation_code_fk)"
	)
	private String nationName;

	public CustomerEntity() {

	}
}
