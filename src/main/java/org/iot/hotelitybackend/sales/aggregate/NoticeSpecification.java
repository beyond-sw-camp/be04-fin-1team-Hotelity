package org.iot.hotelitybackend.sales.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class NoticeSpecification {

    public static Specification<NoticeEntity> equalsBranchCode(String branchCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
    }
}
