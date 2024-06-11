package org.iot.hotelitybackend.employee.repository;

import org.iot.hotelitybackend.employee.aggregate.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
}
