package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.springframework.data.jpa.domain.Specification;

public class StaySpecification {

	public static Specification<StayEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
	}

	public static Specification<StayEntity> equalsRoomLevelName(String roomLevelName) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomLevelName"), roomLevelName);
	}

	public static Specification<StayEntity> equalsStayCheckinTime(LocalDateTime stayCheckinTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckinTime"), stayCheckinTime);
	}

	public static Specification<StayEntity> equalsStayCheckoutTime(LocalDateTime stayCheckoutTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckoutTime"), stayCheckoutTime);
	}


}
