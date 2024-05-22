package org.iot.hotelitybackend.hotelmanagement.aggregate;

import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {
	public static Specification<RoomEntity> equalsRoomCategoryCodeFk(Integer roomCategoryCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCategoryCodeFk"), roomCategoryCodeFk);
	}
	public static Specification<RoomEntity> equalsRoomCurrentStatus(String roomCurrentStatus) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCurrentStatus"), roomCurrentStatus);
	}
	public static Specification<RoomEntity> equalsBranchCodeFk(String branchCodeFk) {
		return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk));
	}

}
