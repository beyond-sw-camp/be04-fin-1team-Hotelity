package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class StaySpecification {

	public static Specification<StayEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationCodeJoin = root.join("reservation");
			Join<ReservationEntity, BranchEntity> branchCodeJoin = reservationCodeJoin.join("branch");

			return criteriaBuilder.equal(branchCodeJoin.get("branchCodePk"), branchCodeFk);
		};
	}

	public static Specification<StayEntity> equalsRoomLevelName(String roomLevelName) {
		return (root, query, CriteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationCodeJoin = root.join("reservation");
			Join<ReservationEntity, RoomEntity> roomCodeJoin = reservationCodeJoin.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryCodeJoin = roomCodeJoin.join("roomCategory");
			Join<RoomCategoryEntity, RoomLevelEntity> roomLevelCodeJoin = roomCategoryCodeJoin.join("roomLevel");

			return CriteriaBuilder.equal(roomLevelCodeJoin.get("roomLevelName"), roomLevelName);
		};
	}

	public static Specification<StayEntity> equalsStayCheckinTime(LocalDateTime stayCheckinTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckinTime"), stayCheckinTime);
	}

	public static Specification<StayEntity> equalsStayCheckoutTime(LocalDateTime stayCheckoutTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckoutTime"), stayCheckoutTime);
	}
}
