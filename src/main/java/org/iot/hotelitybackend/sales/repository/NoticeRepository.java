package org.iot.hotelitybackend.sales.repository;

import java.util.List;

import org.iot.hotelitybackend.sales.aggregate.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer>, JpaSpecificationExecutor<NoticeEntity> {
    Page<NoticeEntity> findAll(Specification<NoticeEntity> spec, Pageable pageable);

	List<NoticeEntity> findTop3ByOrderByNoticeCodePkDesc();
}
