package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
}
