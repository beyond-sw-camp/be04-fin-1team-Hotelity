package org.iot.hotelitybackend.hotelmanagement.repository;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomImageEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Integer> {
	List<RoomImageEntity> findAllByRoomCodeFk(String roomCodePk);
}
