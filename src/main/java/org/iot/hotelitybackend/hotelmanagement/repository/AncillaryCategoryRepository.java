package org.iot.hotelitybackend.hotelmanagement.repository;

import org.iot.hotelitybackend.hotelmanagement.aggregate.AncillaryCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AncillaryCategoryRepository extends JpaRepository<AncillaryCategoryEntity, Integer> {
	AncillaryCategoryEntity findByAncillaryCategoryName(String ancillaryCategoryName);
}
