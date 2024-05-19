package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class StaySpecification {

	public static Specification<StayEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, BranchEntity> branchJoin = reservationJoin.join("branch");

			return criteriaBuilder.equal(branchJoin.get("branchCodePk"), branchCodeFk);
		};
	}

	public static Specification<StayEntity> equalsCustomerName(String customerName) {
		return (root, query, criteriaBuilder) -> {
			if (customerName == null || customerName.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, CustomerEntity> customerJoin = reservationJoin.join("customer");

			return criteriaBuilder.equal(customerJoin.get("customerName"), customerName);
		};
	}

	public static Specification<StayEntity> equalsRoomLevelName(String roomLevelName) {
		return (root, query, CriteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
			Join<RoomCategoryEntity, RoomLevelEntity> roomLevelJoin = roomCategoryJoin.join("roomLevel");


			return CriteriaBuilder.equal(roomLevelJoin.get("roomLevelName"), roomLevelName);
		};
	}

	public static Specification<StayEntity> equalsStayCheckinTime(LocalDateTime stayCheckinTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckinTime"), stayCheckinTime);
	}

	public static Specification<StayEntity> equalsStayCheckoutTime(LocalDateTime stayCheckoutTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckoutTime"), stayCheckoutTime);
	}
}
