package org.iot.hotelitybackend.employee.repository;

import org.iot.hotelitybackend.employee.aggregate.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {
}
