package org.iot.hotelitybackend.marketing.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CampaignRepository extends JpaRepository<CampaignEntity, Integer>, JpaSpecificationExecutor<CampaignEntity> {
    Page<CampaignEntity> findAll(Specification<CampaignEntity> spec, Pageable pageable);

}
