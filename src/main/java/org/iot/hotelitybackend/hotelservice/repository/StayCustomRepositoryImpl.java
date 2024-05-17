package org.iot.hotelitybackend.hotelservice.repository;

import java.util.List;

import org.iot.hotelitybackend.hotelservice.aggregate.QStayEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class StayCustomRepositoryImpl implements StayCustomRepository{
	private final JPAQueryFactory jpaQueryFactory;

	public StayCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public List<StayEntity> findAllByBranchCodeFk() {
		return jpaQueryFactory
			// .selectFrom(stayEntity)

	}
}
