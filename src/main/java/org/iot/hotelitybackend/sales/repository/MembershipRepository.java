package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {
	MembershipEntity findByMembershipLevelCodePk(Integer membershipLevelCodePk);

	MembershipEntity findByMembershipLevelName(String membershipLevelName);

	Page<MembershipEntity> findAll(Specification<MembershipEntity> specification, Pageable pageable);
}
