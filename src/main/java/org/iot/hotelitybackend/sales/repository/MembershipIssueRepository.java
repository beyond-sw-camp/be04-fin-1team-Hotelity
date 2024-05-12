package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipIssueRepository extends JpaRepository<MembershipIssueEntity, Integer> {
}
