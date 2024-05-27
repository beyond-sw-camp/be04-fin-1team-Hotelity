package org.iot.hotelitybackend.employee.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {
    public static Specification<EmployeeEntity> equalsEmployeeCodePk(Integer employeeCodePk) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("employeeCodePk"), employeeCodePk);
    }
    public static Specification<EmployeeEntity> containsEmployeeName(String employeeName) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.like(root.get("employeeName"), "%" + employeeName + "%");
    }

    public static Specification<EmployeeEntity> containsEmployeeAddress(String employeeAddress) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.like(root.get("employeeAddress"), "%" + employeeAddress + "%");
    }

    public static Specification<EmployeeEntity> containsEmployeePhoneNumber(String employeePhoneNumber) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.like(root.get("employeePhoneNumber"), "%" + employeePhoneNumber + "%");
    }

    public static Specification<EmployeeEntity> containsEmployeeOfficePhoneNumber(String employeeOfficePhoneNumber) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.like(root.get("employeeOfficePhoneNumber"), "%" + employeeOfficePhoneNumber + "%");
    }

    public static Specification<EmployeeEntity> containsEmployeeEmail(String employeeEmail) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.like(root.get("employeeEmail"), "%" + employeeEmail + "%");
    }

    public static Specification<EmployeeEntity> equalsEmployeeResignStatus(String employeeResignStatus) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("employeeResignStatus"), employeeResignStatus);
    }

    public static Specification<EmployeeEntity> equalsPermission(Integer permissionCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("permission").get("permissionCodePk"), permissionCode);
    }

    public static Specification<EmployeeEntity> equalsPosition(Integer positionCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("position").get("positionCodePk"), positionCode);
    }

    public static Specification<EmployeeEntity> equalsRank(Integer rankCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("rank").get("rankCodePk"), rankCode);
    }

    public static Specification<EmployeeEntity> equalsDepartment(Integer departmentCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("department").get("departmentCodePk"), departmentCode);
    }

    public static Specification<EmployeeEntity> equalsBranch(String branchCode) {
        return (root, query, CriteriaBuilder) ->
                CriteriaBuilder.equal(root.get("branch").get("branchCodePk"), branchCode);
    }
}
