package org.iot.hotelitybackend.hotelmanagement.aggregate;

import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

public class AncillarySpecification {
	public static Specification<AncillaryEntity> equalsAncillaryCodePk(Integer ancillaryCodePk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("ancillaryCodePk"), ancillaryCodePk);
	}

	public static Specification<AncillaryEntity> likeAncillaryName(String ancillaryName) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("ancillaryName"), "%" + ancillaryName + "%");
	}

	public static Specification<AncillaryEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
	}

	public static Specification<AncillaryEntity> likeAncillaryLocation(String ancillaryLocation) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("ancillaryLocation"), "%" + ancillaryLocation + "%");
	}

	public static Specification<AncillaryEntity> byOpenTimeGreaterThenOrEqual(LocalTime ancillaryOpenTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.greaterThanOrEqualTo(root.get("ancillaryOpenTime"), ancillaryOpenTime);
	}

	public static Specification<AncillaryEntity> byCloseTimeLessThenOrEqual(LocalTime ancillaryCloseTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.lessThanOrEqualTo(root.get("ancillaryCloseTime"), ancillaryCloseTime);
	}

	public static Specification<AncillaryEntity> likeAncillaryPhoneNumber(String ancillaryPhoneNumber) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("ancillaryPhoneNumber"), "%" + ancillaryPhoneNumber + "%");
	}

	public static Specification<AncillaryEntity> equalsAncillaryCategoryCodeFk(Integer ancillaryCategoryCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("ancillaryCategoryCodeFk"), ancillaryCategoryCodeFk);
	}
}
