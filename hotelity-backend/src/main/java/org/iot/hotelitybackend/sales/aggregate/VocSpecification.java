package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.criteria.Join;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class VocSpecification {

    // voc 코드
    public static Specification<VocEntity> equalsVocCodePk(Integer vocCodePk) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vocCodePk"), vocCodePk);
    }

    // voc 제목
    public static Specification<VocEntity> likeVocTitle(String vocTitle){

        String pattern = "%" + vocTitle + "%";

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("vocTitle"), pattern);
    }

    // voc 카테고리
    public static Specification<VocEntity> likeVocCategory(String vocCategory) {

        String pattern = "%" + vocCategory + "%";
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("vocCategory"), pattern);
    }

    // 고객코드
    public static Specification<VocEntity> equalsCustomerCode(Integer customerCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
    }

    // 고객명
    public static Specification<VocEntity> likeCustomerName(String customerName) {

        String pattern = "%" + customerName + "%";

        return (root, query, criteriaBuilder) -> {
            Join<VocEntity, CustomerEntity> customerJoin = root.join("customer");

            return criteriaBuilder.like(customerJoin.get("customerName"), pattern);
        };
    }

    // voc 작성 일자(vocCreatedDate)
    public static Specification<VocEntity> equalsVocCreatedDate(LocalDateTime vocCreatedDate) {

        LocalDate date = vocCreatedDate.toLocalDate();

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("vocCreatedDate").as(LocalDate.class), date);
    }

    // voc 업데이트 일자(vocLastUpdatedDate)
    public static Specification<VocEntity> equalsVocLastUpdatedDate(LocalDateTime vocLastUpdatedDate) {

        LocalDate date = vocLastUpdatedDate.toLocalDate();

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("vocLastUpdatedDate").as(LocalDate.class), date);
    }

    // 지점코드
    public static Specification<VocEntity> equalsBranchCode(String branchCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
    }

    // 직원코드
    public static Specification<VocEntity> equalsEmployeeCodeFk(Integer employeeCodeFk) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employeeCodeFk"), employeeCodeFk);
    }

    // 직원명
    public static Specification<VocEntity> likeEmployeeName(String employeeName) {

        String pattern = "%" + employeeName + "%";

        return (root, query, criteriaBuilder) -> {
            Join<VocEntity, EmployeeEntity> employeeJoin = root.join("employee");

            return criteriaBuilder.like(employeeJoin.get("employeeName"), pattern);
        };
    }

    // voc 처리상태
    public static Specification<VocEntity> equalsVocProcessStatus(Integer vocProcessStatus) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("vocProcessStatus"), vocProcessStatus);
    }
}
