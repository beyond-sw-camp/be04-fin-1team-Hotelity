package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;


public class ReservationSpecification {
	public static Specification<ReservationEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
	}

	public static Specification<ReservationEntity> equalsRoomCodeFk(String roomCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCodeFk"), roomCodeFk);
	}

	public static Specification<ReservationEntity> equalsCheckinDate(LocalDateTime reservationCheckinDate) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("reservationCheckinDate"), reservationCheckinDate);
	}

	public static Specification<ReservationEntity> equalsCheckoutDate(LocalDateTime reservationCheckoutDate) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("reservationCheckoutDate"), reservationCheckoutDate);
	}
}
