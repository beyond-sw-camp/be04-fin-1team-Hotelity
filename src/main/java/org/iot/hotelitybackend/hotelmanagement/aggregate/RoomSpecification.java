package org.iot.hotelitybackend.hotelmanagement.aggregate;

import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;

public class RoomSpecification {
	public static Specification<RoomEntity> equalsRoomName(Integer roomCategoryCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCategoryCodeFk"), roomCategoryCodeFk);
	}
	public static Specification<RoomEntity> equalsRoomCurrentStatus(String roomCurrentStatus) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCurrentStatus"), roomCurrentStatus);
	}

}
