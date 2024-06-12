package org.iot.hotelitybackend.sales.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class MembershipSpecification {
	public static Specification<MembershipEntity> equalsMembershipLevelCodePk(Integer membershipLevelCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("membershipLevelCodePk"), membershipLevelCodePk);
	}

	public static Specification<MembershipEntity> likesMembershipInfo(String membershipInfo) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("membershipInfo"), "%" + membershipInfo + "%");
	}

	public static Specification<MembershipEntity> equalsMembershipCriteriaAmount(Integer membershipCriteriaAmount) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("membershipCriteriaAmount"), membershipCriteriaAmount);
	}

	public static Specification<MembershipEntity> likesMembershipLevelName(String membershipLevelName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("membershipLevelName"), "%" + membershipLevelName + "%");
	}
}
