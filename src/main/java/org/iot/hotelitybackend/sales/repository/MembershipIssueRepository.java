package org.iot.hotelitybackend.sales.repository;

import java.util.List;

import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.sales.dto.MembershipIssueDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipIssueRepository extends JpaRepository<MembershipIssueEntity, Integer> {
	MembershipIssueEntity findByCustomerCodeFk(int customerCodeFk);

	List<MembershipIssueEntity> findMembershipByCustomerCodeFk(int customerCodeFk);
}
