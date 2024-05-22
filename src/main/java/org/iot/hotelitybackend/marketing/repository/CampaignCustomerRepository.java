package org.iot.hotelitybackend.marketing.repository;

import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignCustomerRepository extends JpaRepository<CampaignCustomerEntity, Integer> {
}
