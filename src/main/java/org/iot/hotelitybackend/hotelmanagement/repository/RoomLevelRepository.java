package org.iot.hotelitybackend.hotelmanagement.repository;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomLevelRepository extends JpaRepository<RoomLevelEntity, Integer> {
	RoomLevelEntity findByRoomLevelName(String roomLevelName);

}
