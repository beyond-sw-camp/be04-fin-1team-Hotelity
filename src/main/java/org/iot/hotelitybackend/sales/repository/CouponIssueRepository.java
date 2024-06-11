package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.CouponIssueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CouponIssueRepository extends JpaRepository<CouponIssueEntity, Integer>, JpaSpecificationExecutor<CouponIssueEntity> {
    Page<CouponIssueEntity> findAll(Specification<CouponIssueEntity> spec, Pageable pageable);
}
