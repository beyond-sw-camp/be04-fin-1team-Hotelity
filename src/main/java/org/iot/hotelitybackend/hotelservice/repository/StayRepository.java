package org.iot.hotelitybackend.hotelservice.repository;

import java.util.Optional;

import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayRepository extends JpaRepository<StayEntity, Integer> {
	Optional<StayEntity> findByReservationCodeFk(int reservationCodePk);
}
