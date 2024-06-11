package org.iot.hotelitybackend.hotelmanagement.repository;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMaintenanceRepository extends JpaRepository<RoomMaintenanceEntity, Integer> {
}
