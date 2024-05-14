package org.iot.hotelitybackend.sales.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class CouponIssueSpecification {

    public static Specification<CouponIssueEntity> equalsCustomerCode(Integer customerCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
    }

}
