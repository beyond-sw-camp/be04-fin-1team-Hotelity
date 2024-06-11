package org.iot.hotelitybackend.sales.aggregate;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

public class CouponSpecification {

	public static Specification<CouponEntity> equalsCouponCodePk(Integer couponCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponCodePk"), couponCodePk);
	}

	public static Specification<CouponEntity> likesCouponName(String couponName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("couponName"), "%" + couponName + "%");
	}

	public static Specification<CouponEntity> likesCouponType(String couponType) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("couponType"), "%" + couponType + "%");
	}

	public static Specification<CouponEntity> equalsCouponDiscountRate(Double couponDiscountRate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponDiscountRate"), couponDiscountRate);
	}

	public static Specification<CouponEntity> equalsCouponLaunchingDate(Date couponLaunchingDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponLaunchingDate"), couponLaunchingDate);
	}

	public static Specification<CouponEntity> likesCouponInfo(String couponInfo) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponInfo"), couponInfo);
	}
}
