package org.iot.hotelitybackend.customer.aggregate;

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
}
