package org.iot.hotelitybackend.hotelservice.repository;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.aggregate.QBranchEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.QReservationEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.QStayEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface StayCustomRepository {

	BooleanExpression equalsBranchCodeFk(String branchCodeFk);


}
