package org.iot.hotelitybackend.customer.repository;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
}
