package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class StaySpecification {

	// // 투숙코드
	// public static Specification<StayEntity> equalsStayCodePk(Integer stayCodePk) {
	// 	return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stayCodePk"), stayCodePk);
	// }
	//
	// // 고객코드
	// public static Specification<StayEntity> equalsCustomerCodeFk(Integer customerCodeFk) {
	// 	return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
	// }
	//
	// // 고객이름
	// public static Specification<StayEntity> likeCustomerName(String customerName) {
	//
	// 	String pattern = "%" + customerName + "%";
	//
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, CustomerEntity> customerJoin = reservationJoin.join("customer");
	//
	// 		return criteriaBuilder.like(customerJoin.get("customerName"), pattern);
	// 	};
	// }
	//
	// // 객실코드
	// public static Specification<StayEntity> equalsRoomCodeFk(String roomCodeFk) {
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
	//
	// 		return criteriaBuilder.equal(roomJoin.get("roomCodeFk"), roomCodeFk);
	// 	};
	// }
	//
	// // 객실명
	// public static Specification<StayEntity> likeRoomName(String roomName) {
	//
	// 	String pattern = "%" + roomName + "%";
	//
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
	// 		Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
	//
	// 		return criteriaBuilder.like(roomCategoryJoin.get("roomName"), pattern);
	// 	};
	// }
	//
	// // 객실 등급명
	// public static Specification<StayEntity> likeRoomLevelName(String roomLevelName) {
	//
	// 	String pattern = "%" + roomLevelName + "%";
	// 	return (root, query, CriteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
	// 		Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
	// 		Join<RoomCategoryEntity, RoomLevelEntity> roomLevelJoin = roomCategoryJoin.join("roomLevel");
	//
	//
	// 		return CriteriaBuilder.equal(roomLevelJoin.get("roomLevelName"), pattern);
	// 	};
	// }
	//
	// // 객실 수용 인원
	// public static Specification<StayEntity> equalsRoomCapacity(Integer roomCapacity) {
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
	// 		Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
	//
	// 		return criteriaBuilder.equal(roomCategoryJoin.get("roomCapacity"), roomCapacity);
	// 	};
	// }
	//
	// // 투숙 인원
	// public static Specification<StayEntity> equalsStayPeopleCount(Integer stayPeopleCount) {
	// 	return (root, query, criteriaBuilder) ->
	// 		criteriaBuilder.equal(root.get("stayPeopleCount"), stayPeopleCount);
	// }
	//
	// // 지점코드
	// public static Specification<StayEntity> equalsBranchCodeFk(String branchCodeFk) {
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, BranchEntity> branchJoin = reservationJoin.join("branch");
	//
	// 		return criteriaBuilder.equal(branchJoin.get("branchCodePk"), branchCodeFk);
	// 	};
	// }
	//
	// // 체크인 날짜
	// public static Specification<StayEntity> equalsStayCheckinTime(LocalDateTime stayCheckinTime) {
	//
	// 	LocalDate date = stayCheckinTime.toLocalDate();
	//
	// 	return (root, query, CriteriaBuilder) ->
	// 		CriteriaBuilder.equal(root.get("stayCheckinTime").as(LocalDate.class), date);
	// }
	//
	// // 체크아웃 날짜
	// public static Specification<StayEntity> equalsStayCheckoutTime(LocalDateTime stayCheckoutTime) {
	//
	// 	LocalDate date = stayCheckoutTime.toLocalDate();
	//
	// 	return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckoutTime"), stayCheckoutTime);
	// }
	//
	// // 직원코드
	// public static Specification<StayEntity> equalsEmployeeCodeFk(Integer employeeCodeFk) {
	// 	return (root, query, criteriaBuilder) ->
	// 		criteriaBuilder.equal(root.get("employeeCodeFk"), employeeCodeFk);
	// }
	//
	// // 직원이름
	// public static Specification<StayEntity> likeEmployeeName(String employeeName) {
	//
	// 	String pattern = "%" + employeeName + "%";
	//
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, EmployeeEntity> employeeJoin = root.join("employee");
	//
	// 		return criteriaBuilder.like(employeeJoin.get("employeeName"), pattern);
	// 	};
	// }
	//
	// // 예약코드
	// public static Specification<StayEntity> equalsReservationCodeFk(Integer reservationCodeFk) {
	// 	return (root, query, criteriaBuilder) ->
	// 		criteriaBuilder.equal(root.get("reservationCodeFk"), reservationCodeFk);
	// }

	public static Specification<StayEntity> equalsStayCodePk(Integer stayCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stayCodePk"), stayCodePk);
	}

	public static Specification<StayEntity> equalsCustomerCodeFk(Integer customerCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
	}

	public static Specification<StayEntity> likeCustomerName(String customerName) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Join<StayEntity, ReservationEntity> reservationJoin = subquery.from(ReservationEntity.class).join("customer");
			subquery.select(reservationJoin.get("reservationCodePk"))
				.where(criteriaBuilder.like(reservationJoin.get("customer").get("customerName"), "%" + customerName + "%"));
			return criteriaBuilder.in(root.get("reservationCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> equalsRoomCodeFk(String roomCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Join<StayEntity, ReservationEntity> reservationJoin = subquery.from(ReservationEntity.class).join("room");
			subquery.select(reservationJoin.get("reservationCodePk"))
				.where(criteriaBuilder.equal(reservationJoin.get("room").get("roomCodeFk"), roomCodeFk));
			return criteriaBuilder.in(root.get("reservationCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> likeRoomName(String roomName) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Join<StayEntity, ReservationEntity> reservationJoin = subquery.from(ReservationEntity.class).join("room").join("roomCategory");
			subquery.select(reservationJoin.get("reservationCodePk"))
				.where(criteriaBuilder.like(reservationJoin.get("roomCategory").get("roomName"), "%" + roomName + "%"));
			return criteriaBuilder.in(root.get("reservationCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> likeRoomLevelName(String roomLevelName) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Join<StayEntity, ReservationEntity> reservationJoin = subquery.from(ReservationEntity.class).join("room").join("roomCategory").join("roomLevel");
			subquery.select(reservationJoin.get("reservationCodePk"))
				.where(criteriaBuilder.like(reservationJoin.get("roomLevel").get("roomLevelName"), "%" + roomLevelName + "%"));
			return criteriaBuilder.in(root.get("reservationCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> equalsRoomCapacity(Integer roomCapacity) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Join<StayEntity, ReservationEntity> reservationJoin = subquery.from(ReservationEntity.class).join("room").join("roomCategory");
			subquery.select(reservationJoin.get("reservationCodePk"))
				.where(criteriaBuilder.equal(reservationJoin.get("roomCategory").get("roomCapacity"), roomCapacity));
			return criteriaBuilder.in(root.get("reservationCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> equalsStayPeopleCount(Integer stayPeopleCount) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stayPeopleCount"), stayPeopleCount);
	}

	public static Specification<StayEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Join<StayEntity, ReservationEntity> reservationJoin = subquery.from(ReservationEntity.class).join("branch");
			subquery.select(reservationJoin.get("reservationCodePk"))
				.where(criteriaBuilder.equal(reservationJoin.get("branch").get("branchCodePk"), branchCodeFk));
			return criteriaBuilder.in(root.get("reservationCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> equalsStayCheckinTime(LocalDateTime stayCheckinTime) {
		LocalDate date = stayCheckinTime.toLocalDate();
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stayCheckinTime").as(LocalDate.class), date);
	}

	public static Specification<StayEntity> equalsStayCheckoutTime(LocalDateTime stayCheckoutTime) {
		LocalDate date = stayCheckoutTime.toLocalDate();
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stayCheckoutTime").as(LocalDate.class), date);
	}

	public static Specification<StayEntity> equalsEmployeeCodeFk(Integer employeeCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employeeCodeFk"), employeeCodeFk);
	}

	public static Specification<StayEntity> likeEmployeeName(String employeeName) {
		return (root, query, criteriaBuilder) -> {
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<EmployeeEntity> employeeJoin = subquery.from(EmployeeEntity.class);
			subquery.select(employeeJoin.get("employeeCodePk"))
				.where(criteriaBuilder.like(employeeJoin.get("employeeName"), "%" + employeeName + "%"));
			return criteriaBuilder.in(root.get("employeeCodeFk")).value(subquery);
		};
	}

	public static Specification<StayEntity> equalsReservationCodeFk(Integer reservationCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCodeFk"), reservationCodeFk);
	}
}
