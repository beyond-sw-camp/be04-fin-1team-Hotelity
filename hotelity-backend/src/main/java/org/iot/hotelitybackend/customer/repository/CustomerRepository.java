package org.iot.hotelitybackend.customer.repository;

import java.util.List;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>,
	JpaSpecificationExecutor<CustomerEntity> {

	CustomerEntity findByCustomerName(String customerName);

	CustomerEntity findByCustomerEmail(String address);

	List<CustomerEntity> findAllByCustomerName(String customerName);

}
