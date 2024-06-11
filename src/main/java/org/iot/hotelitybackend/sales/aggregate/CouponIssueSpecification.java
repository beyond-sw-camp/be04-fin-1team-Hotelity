package org.iot.hotelitybackend.sales.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.springframework.data.jpa.domain.Specification;

public class CouponIssueSpecification {

	public static Specification<CouponIssueEntity> equalsCouponIssueCodePk(Integer couponIssueCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponIssueCodePk"), couponIssueCodePk);
	}

	public static Specification<CouponIssueEntity> likeCouponName(String couponName) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var coupon = subquery.from(CouponEntity.class);
			subquery.select(coupon.get("id"))
				.where(
					criteriaBuilder.like(coupon.get("couponName"), "%" + couponName + "%")
				);
			return criteriaBuilder.in(root.get("couponCodeFk")).value(subquery);
		};
	}

	public static Specification<CouponIssueEntity> likeCustomerName(String customerName) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var customer = subquery.from(CustomerEntity.class);
			subquery.select(customer.get("id"))
				.where(
					criteriaBuilder.like(customer.get("customerName"), "%" + customerName + "%")
				);
			return criteriaBuilder.in(root.get("customerCodeFk")).value(subquery);
		};
	}

	public static Specification<CouponIssueEntity> equalsCouponDiscountRate(Double couponDiscountRate) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var coupon = subquery.from(CouponEntity.class);
			subquery.select(coupon.get("id"))
				.where(
					criteriaBuilder.equal(coupon.get("couponDiscountRate"), couponDiscountRate)
				);
			return criteriaBuilder.in(root.get("couponCodeFk")).value(subquery);
		};
	}

	public static Specification<CouponIssueEntity> equalsCouponIssueDate(LocalDateTime couponIssueDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponIssueDate"), couponIssueDate);
	}

	public static Specification<CouponIssueEntity> equalsCouponExpireDate(LocalDateTime couponExpireDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponExpireDate"), couponExpireDate);
	}

	public static Specification<CouponIssueEntity> equalsCouponUseDate(LocalDateTime couponUseDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("couponUseDate"), couponUseDate);
	}

	public static Specification<CouponIssueEntity> equalsCustomerCodePk(Integer customerCodePk) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var customer = subquery.from(CustomerEntity.class);
			subquery.select(customer.get("id")).where(
				criteriaBuilder.equal(customer.get("customerCodePk"), customerCodePk)
			);
			return criteriaBuilder.in(root.get("customerCodeFk")).value(subquery);
		};	}
}
