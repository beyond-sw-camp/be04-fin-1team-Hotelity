package org.iot.hotelitybackend.customer.repository;

import org.iot.hotelitybackend.customer.aggregate.NationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationRepository extends JpaRepository<NationEntity, Integer> {
}
