package org.iot.hotelitybackend.marketing.repository;

import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CampaignCustomerRepository extends JpaRepository<CampaignCustomerEntity, Integer>,
	JpaSpecificationExecutor<CampaignCustomerEntity> {
	Page<CampaignCustomerEntity> findAll(Specification<CampaignCustomerEntity> spec, Pageable pageable);
}
