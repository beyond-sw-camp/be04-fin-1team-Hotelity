package org.iot.hotelitybackend.employee.repository;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
}
