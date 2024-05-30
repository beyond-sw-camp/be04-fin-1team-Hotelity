package org.iot.hotelitybackend.customer.aggregate;

import java.util.Date;

import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class CustomerSpecification {
	public static Specification<CustomerEntity> equalsCustomerType(String customerType) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerType"), customerType);
	}

	public static Specification<CustomerEntity> equalsMembershipLevelName(String membershipLevelName) {
		return (root, query, criteriaBuilder) -> {
			Join<CustomerEntity, MembershipIssueEntity> issueJoin = root.join("membershipIssues");
			Join<MembershipIssueEntity, MembershipEntity> membershipJoin = issueJoin.join("membership");
			return criteriaBuilder.equal(membershipJoin.get("membershipLevelName"), membershipLevelName);
		};
	}

	public static Specification<CustomerEntity> equalsCustomerCodePk(int customerCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerCodePk"), customerCodePk);
	}

	public static Specification<CustomerEntity> equalsCustomerName(String customerName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%");
	}

	public static Specification<CustomerEntity> equalsCustomerEmail(String customerEmail) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerEmail"), customerEmail);
	}

	public static Specification<CustomerEntity> equalsCustomerPhoneNumber(String customerPhoneNumber) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("customerPhoneNumber"), "%" + customerPhoneNumber + "%");
	}

	public static Specification<CustomerEntity> equalsCustomerEnglishName(String customerEnglishName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerEnglishName"), customerEnglishName);
	}

	public static Specification<CustomerEntity> equalsCustomerAddress(String customerAddress) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerAddress"), customerAddress);
	}

	public static Specification<CustomerEntity> equalsCustomerInfoAgreement(int customerInfoAgreement) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerInfoAgreement"), customerInfoAgreement);
	}

	public static Specification<CustomerEntity> equalsCustomerStatus(int customerStatus) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerStatus"), customerStatus);
	}

	public static Specification<CustomerEntity> equalsCustomerRegisteredDate(Date customerRegisteredDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerRegisteredDate"), customerRegisteredDate);
	}

	public static Specification<CustomerEntity> equalsNationCodeFk(int nationCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nationCodeFk"), nationCodeFk);
	}

	public static Specification<CustomerEntity> equalsCustomerGender(String customerGender) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerGender"), customerGender);
	}

	public static Specification<CustomerEntity> equalsNationName(String nationName) {
		return (root, query, criteriaBuilder) ->
		{
			Join<CustomerEntity, NationEntity> joinNation = root.join("nation");
			return criteriaBuilder.equal(joinNation.get("nationName"), nationName);
		};
	}
}
