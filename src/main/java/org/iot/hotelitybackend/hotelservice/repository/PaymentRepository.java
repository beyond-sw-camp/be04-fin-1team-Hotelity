package org.iot.hotelitybackend.hotelservice.repository;

import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer>, JpaSpecificationExecutor<PaymentEntity> {
	Page<PaymentEntity> findAll(Specification<PaymentEntity> spec, Pageable pageable);
}
