package org.iot.hotelitybackend.marketing.repository;

import org.iot.hotelitybackend.marketing.aggregate.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Integer> {
}
