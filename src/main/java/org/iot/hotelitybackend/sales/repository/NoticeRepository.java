package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
    Page<NoticeEntity> findAll(Specification<NoticeEntity> spec, Pageable pageable);
}
