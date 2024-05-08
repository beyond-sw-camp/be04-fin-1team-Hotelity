package org.iot.hotelitybackend.hotelmanagement.repository;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, String> {
}
