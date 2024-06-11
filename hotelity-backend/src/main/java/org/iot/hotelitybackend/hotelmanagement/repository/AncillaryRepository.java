package org.iot.hotelitybackend.hotelmanagement.repository;

import org.iot.hotelitybackend.hotelmanagement.aggregate.AncillaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AncillaryRepository extends JpaRepository<AncillaryEntity, Integer>,
	JpaSpecificationExecutor<AncillaryEntity> {
}
