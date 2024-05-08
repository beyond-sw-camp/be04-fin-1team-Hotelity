package org.iot.hotelitybackend.employee.repository;

import org.iot.hotelitybackend.employee.aggregate.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {
}
