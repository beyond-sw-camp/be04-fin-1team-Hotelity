package org.iot.hotelitybackend.hotelservice.repository;

import java.util.Optional;

import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StayRepository extends JpaRepository<StayEntity, Integer>, JpaSpecificationExecutor<StayEntity> {
	Optional<StayEntity> findByReservationCodeFk(int reservationCodePk);
}
