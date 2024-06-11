package org.iot.hotelitybackend.hotelmanagement.repository;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.aggregate.AncillaryImageEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.AncillaryImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AncillaryImageRepository extends JpaRepository<AncillaryImageEntity, Integer> {
	List<AncillaryImageEntity> findAllByAncillaryCodeFk(int ancillaryCodePk);
}
