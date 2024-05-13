package org.iot.hotelitybackend.sales.repository;

import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {
}
