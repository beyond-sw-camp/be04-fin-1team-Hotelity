package org.iot.hotelitybackend.hotelservice.repository;

import java.time.LocalDate;
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

	// List<ReservationEntity> findByReservationCheckinDate(LocalDateTime reservationCheckDate);
	default List<ReservationEntity> findByReservationCheckinDate(LocalDateTime reservationCheckDate) {
		LocalDateTime startOfDay = reservationCheckDate.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime endOfDay = reservationCheckDate.withHour(23).withMinute(59).withSecond(59);
		return findByReservationCheckinDateBetween(startOfDay, endOfDay);
	}

	Page<ReservationEntity> findAll(Specification<ReservationEntity> spec, Pageable pageable);

	List<ReservationEntity> findTop3ByOrderByReservationDateDesc();

	List<ReservationEntity> findAllByReservationCheckinDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
