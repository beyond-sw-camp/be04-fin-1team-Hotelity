package org.iot.hotelitybackend.hotelservice.repository;

import static org.iot.hotelitybackend.hotelservice.aggregate.QStayEntity.*;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.aggregate.QBranchEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.QReservationEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.QStayEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class StayCustomRepositoryImpl implements StayCustomRepository{
	private final JPAQueryFactory jpaQueryFactory;

	public StayCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public BooleanExpression equalsBranchCodeFk(String branchCodeFk) {
		QStayEntity stayEntity = QStayEntity.stayEntity;
		QReservationEntity reservationEntity = QReservationEntity.reservationEntity;
		QBranchEntity branchEntity = QBranchEntity.branchEntity;

		return null;
	}
}
