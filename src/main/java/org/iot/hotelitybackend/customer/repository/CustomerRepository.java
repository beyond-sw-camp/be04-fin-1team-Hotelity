package org.iot.hotelitybackend.customer.repository;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>,
	JpaSpecificationExecutor<CustomerEntity> {
	CustomerEntity findByCustomerName(String customerName);
}
