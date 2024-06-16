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

	default List<StayEntity> findByStayCheckoutTime(LocalDateTime stayCheckoutTime) {
		LocalDateTime startOfDay = stayCheckoutTime.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime endOfDay = stayCheckoutTime.withHour(23).withMinute(59).withSecond(59);
		return findAllByStayCheckinTimeBetween(startOfDay, endOfDay);
	}
}
