package org.iot.hotelitybackend.hotelmanagement.aggregate;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;

public class RoomSpecification {
	public static Specification<RoomEntity> likeRoomCodePk(String roomCodePk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("roomCodePk"), roomCodePk);
	}
	public static Specification<RoomEntity> equalsRoomNumber(Integer roomNumber) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomNumber"), roomNumber);
	}
	public static Specification<RoomEntity> equalsRoomCategoryCodeFk(Integer roomCategoryCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCategoryCodeFk"), roomCategoryCodeFk);
	}
	public static Specification<RoomEntity> equalsRoomCurrentStatus(String roomCurrentStatus) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCurrentStatus"), roomCurrentStatus);
	}
	public static Specification<RoomEntity> equalsRoomDiscountRate(Float roomDiscountRate) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomDiscountRate"), roomDiscountRate);
	}
	public static Specification<RoomEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
	}
	public static Specification<RoomEntity> likeRoomView(String roomView) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("roomView"), roomView);
	}
}
