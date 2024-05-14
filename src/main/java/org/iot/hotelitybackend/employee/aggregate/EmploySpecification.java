package org.iot.hotelitybackend.employee.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class EmploySpecification {
    public static Specification<EmployeeEntity> equalsBranch(String branchCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("branch").get("branchCodePk"), branchCode);
    }

    public static Specification<EmployeeEntity> equalsDepartment(Integer departmentCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("department").get("departmentCodePk"), departmentCode);
    }

    public static Specification<EmployeeEntity> containsEmployeeName(String employeeName) {
        return (root, query, CriteriaBuilder) ->
//            System.out.println(root.get("employeeName"));
            CriteriaBuilder.like(root.get("employeeName"), "%" + employeeName + "%");
    }
}
