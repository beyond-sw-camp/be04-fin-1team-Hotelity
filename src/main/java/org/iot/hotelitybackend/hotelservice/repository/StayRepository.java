package org.iot.hotelitybackend.hotelservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StayRepository extends JpaRepository<StayEntity, Integer>, JpaSpecificationExecutor<StayEntity> {
	Optional<StayEntity> findByReservationCodeFk(int reservationCodePk);

	List<StayEntity> findAllByStayCheckinTimeBetween(LocalDateTime start, LocalDateTime end);
}
