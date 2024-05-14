package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
}
