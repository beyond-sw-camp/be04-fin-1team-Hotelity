package org.iot.hotelitybackend.sales.repository;

import java.util.List;

import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipIssueRepository extends JpaRepository<MembershipIssueEntity, Integer> {
	MembershipIssueEntity findAllByCustomerCodeFk(int customerCodeFk);
}
