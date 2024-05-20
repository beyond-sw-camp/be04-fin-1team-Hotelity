package org.iot.hotelitybackend.customer.repository;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>,
	JpaSpecificationExecutor<CustomerEntity> {

	CustomerEntity findByCustomerName(String customerName);
}
