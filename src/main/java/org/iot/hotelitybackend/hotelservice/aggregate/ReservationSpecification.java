package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class ReservationSpecification {

	// 예약코드
	public static Specification<ReservationEntity> equalsReservationCodePk(Integer reservationCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCodePk"), reservationCodePk);
	}

	// 고객코드
	public static Specification<ReservationEntity> equalsCustomerCodeFk(Integer customerCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
	}

	// 한글이름
	public static Specification<ReservationEntity> likeCustomerName(String customerName) {

		String pattern = "%" + customerName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, CustomerEntity> customerJoin = root.join("customer");

			return criteriaBuilder.like(customerJoin.get("customerName"), pattern);
		};
	}

	// 영어이름
	public static Specification<ReservationEntity> likeCustomerEnglishName(String customerEnglishName) {

		String pattern =  "%" + customerEnglishName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, CustomerEntity> customerJoin = root.join("customer");

			return criteriaBuilder.like(customerJoin.get("customerEnglishName"), pattern);
		};
	}

	// 객실 코드
	public static Specification<ReservationEntity> equalsRoomCodeFk(String roomCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCodeFk"), roomCodeFk);
	}

	// 동작 확인 필요
	// 객실명
	public static Specification<ReservationEntity> equalsRoomName(String roomName) {

		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, RoomEntity> roomJoin = root.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");

			return criteriaBuilder.equal(roomCategoryJoin.get("roomName"), roomName);
		};
	}

	// 동작 확인 필요
	// 객실등급명
	public static Specification<ReservationEntity> equalsRoomLevelName(String roomLevelName) {
		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, RoomEntity> roomJoin = root.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
			Join<RoomCategoryEntity, RoomLevelEntity> roomLevelJoin = roomCategoryJoin.join("roomLevel");

			return criteriaBuilder.equal(roomLevelJoin.get("roomLevelName"), roomLevelName);
		};
	}

	// 동작 확인 필요
	// 객실수용인원
	public static Specification<ReservationEntity> equalsRoomCapacity(Integer roomCapacity) {
		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, RoomEntity> roomJoin = root.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");

			return criteriaBuilder.equal(roomCategoryJoin.get("roomCapacity"), roomCapacity);
		};
	}

	// 지점코드
	public static Specification<ReservationEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
	}

	// 예약일자
	public static Specification<ReservationEntity> equalsReservationDate(LocalDateTime reservationDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationDate"), reservationDate);
	}

	// 체크인일자
	public static Specification<ReservationEntity> equalsCheckinDate(LocalDateTime reservationCheckinDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCheckinDate"), reservationCheckinDate);
	}

	// 체크아웃일자
	public static Specification<ReservationEntity> equalsCheckoutDate(LocalDateTime reservationCheckoutDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCheckoutDate"), reservationCheckoutDate);
	}

	// 예약취소여부
	public static Specification<ReservationEntity> equalsReservationCancleStatus(Integer reservationCancleStatus) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCancleStatus"), reservationCancleStatus);
	}
}
