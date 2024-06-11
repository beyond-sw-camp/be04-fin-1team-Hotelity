package org.iot.hotelitybackend.hotelservice.repository;

import org.iot.hotelitybackend.hotelservice.aggregate.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Integer> {
}
