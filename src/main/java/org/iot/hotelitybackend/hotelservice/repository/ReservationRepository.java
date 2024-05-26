package org.iot.hotelitybackend.hotelservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer>,
	JpaSpecificationExecutor<ReservationEntity> {

	List<ReservationEntity> findByReservationCheckinDateBetween(LocalDateTime start, LocalDateTime end);

	List<ReservationEntity> findByReservationCheckinDate(LocalDateTime reservationCheckDate);

	Page<ReservationEntity> findAll(Specification<ReservationEntity> spec, Pageable pageable);
}
