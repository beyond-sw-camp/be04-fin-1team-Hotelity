package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.VocEntity;
import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VocRepository extends JpaRepository<VocEntity, Integer>, JpaSpecificationExecutor<VocEntity> {
    Page<VocEntity> findAll(Specification<VocEntity> spec, Pageable pageable);
}
