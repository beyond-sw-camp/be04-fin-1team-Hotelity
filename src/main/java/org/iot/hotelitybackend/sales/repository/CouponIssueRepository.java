package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueRepository extends JpaRepository<CouponIssueEntity, Integer> {
}
