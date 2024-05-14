package org.iot.hotelitybackend.marketing.repository;

import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<CampaignEntity, Integer> {
}
