package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class VocSpecification {

    public static Specification<VocEntity> equalsBranchCode(String branchCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
    }

    public static Specification<VocEntity> equalsVocProcessStatus(Integer vocProcessStatus) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("vocProcessStatus"), vocProcessStatus);
    }

    public static Specification<VocEntity> equalsVocCategory(String vocCategory) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("vocCategory"), vocCategory);
    }

    public static Specification<VocEntity> equalsVocCreatedDate(Date vocCreatedDate) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("vocCreatedDate"), vocCreatedDate);
    }

    public static Specification<VocEntity> equalsCustomerCode(Integer customerCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
    }
}
