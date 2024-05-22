package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer>, JpaSpecificationExecutor<CouponEntity> {
    Page<CouponEntity> findAll(Specification<CouponEntity> spec, Pageable pageable);
}
