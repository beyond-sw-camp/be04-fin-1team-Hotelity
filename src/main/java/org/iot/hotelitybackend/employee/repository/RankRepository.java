package org.iot.hotelitybackend.employee.repository;

import org.iot.hotelitybackend.employee.aggregate.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<RankEntity, Integer> {
}
