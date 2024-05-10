package org.iot.hotelitybackend.hotelservice.repository;

import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
}
