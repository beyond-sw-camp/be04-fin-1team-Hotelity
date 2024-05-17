package org.iot.hotelitybackend.hotelservice.repository;

import java.util.List;

import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;

public interface StayCustomRepository {

	List<StayEntity> findAllByBranchCodeFk();
}
