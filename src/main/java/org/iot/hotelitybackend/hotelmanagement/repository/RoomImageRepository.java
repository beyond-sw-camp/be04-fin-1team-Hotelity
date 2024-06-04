package org.iot.hotelitybackend.hotelmanagement.repository;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Integer> {
}
