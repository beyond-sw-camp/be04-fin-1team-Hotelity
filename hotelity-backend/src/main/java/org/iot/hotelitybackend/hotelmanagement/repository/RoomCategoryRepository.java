package org.iot.hotelitybackend.hotelmanagement.repository;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCategoryRepository extends JpaRepository<RoomCategoryEntity, Integer> {
	RoomCategoryEntity findByRoomName(String roomName);
	List<RoomCategoryEntity> findAllByRoomSubRoomsCount(Integer roomSubRoomsCount);
}
