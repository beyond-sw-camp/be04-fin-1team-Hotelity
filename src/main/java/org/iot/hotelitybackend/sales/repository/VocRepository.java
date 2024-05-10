package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.VocEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocRepository extends JpaRepository<VocEntity, Integer> {
}
