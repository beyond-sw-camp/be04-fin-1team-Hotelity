package org.iot.hotelitybackend.hotelservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

	List<ReservationEntity> findByReservationCheckinDateBetween(LocalDateTime start, LocalDateTime end);
}
