package org.iot.hotelitybackend.sales.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class CouponSpecification {

    public static Specification<CouponEntity> equalsCouponDiscountRate(Float couponDiscountRate) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("couponDiscountRate"), couponDiscountRate);
    }

    public static Specification<CouponEntity> equalsCouponType(String couponType) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("couponType"), couponType);
    }
}
