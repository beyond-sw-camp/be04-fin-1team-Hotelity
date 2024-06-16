package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipIssueRepository extends JpaRepository<MembershipIssueEntity, Integer> {
	MembershipIssueEntity findByCustomerCodeFk(int customerCodeFk);

	MembershipIssueEntity findTopByCustomerCodeFkOrderByMembershipIssueDateDesc(Integer customerCodeFk);

	List<MembershipIssueEntity> findMembershipByCustomerCodeFk(int customerCodePk);

	List<MembershipIssueEntity> findAllByMembershipLevelCodeFk(Integer membershipLevelCodePk);

	void deleteByCustomerCodeFk(int customerCodePk);
}
