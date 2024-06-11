package org.iot.hotelitybackend.employee.repository;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer>, JpaSpecificationExecutor<EmployeeEntity> {

    Page<EmployeeEntity> findAll(Specification<EmployeeEntity> spec, Pageable pageable);

    Boolean existsByEmployeeNameAndEmployeePhoneNumberAndEmployeeEmail(String employeeName, String employeePhoneNumber, String employeeEmail);

    EmployeeEntity findByBranchAndEmployeeCodePk(BranchEntity branch, Integer employeeCode);

}
