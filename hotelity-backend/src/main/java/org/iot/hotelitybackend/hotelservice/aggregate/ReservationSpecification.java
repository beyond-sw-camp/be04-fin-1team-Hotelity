package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class ReservationSpecification {

	// 특정 월의 예약 건만 찾는 조건
	public static Specification<ReservationEntity> betweenDate(LocalDateTime startOfMonth, LocalDateTime endOfMonth) {

		LocalDate start = startOfMonth.toLocalDate();
		LocalDate end = endOfMonth.toLocalDate();

		return (root, query, criteriaBuilder) ->
			criteriaBuilder.between(root.get("reservationCheckinDate").as(LocalDate.class), start, end);
	}

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
	public static Specification<ReservationEntity> likeRoomCodeFk(String roomCodeFk) {

		String pattern = "%" + roomCodeFk + "%";

		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("roomCodeFk"), pattern);
	}

	// 객실명
	public static Specification<ReservationEntity> likeRoomName(String roomName) {

		String pattern = "%" + roomName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, RoomEntity> roomJoin = root.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");

			return criteriaBuilder.like(roomCategoryJoin.get("roomName"), pattern);
		};
	}

	// 객실등급명
	public static Specification<ReservationEntity> likeRoomLevelName(String roomLevelName) {

		String pattern = "%" + roomLevelName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<ReservationEntity, RoomEntity> roomJoin = root.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
			Join<RoomCategoryEntity, RoomLevelEntity> roomLevelJoin = roomCategoryJoin.join("roomLevel");

			return criteriaBuilder.like(roomLevelJoin.get("roomLevelName"), pattern);
		};
	}

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
		LocalDate date = reservationDate.toLocalDate();
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("reservationDate").as(LocalDate.class), date);
	}

	// 체크인일자
	public static Specification<ReservationEntity> equalsCheckinDate(LocalDateTime reservationCheckinDate) {
		LocalDate date = reservationCheckinDate.toLocalDate();

		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("reservationCheckinDate").as(LocalDate.class), date);
	}

	// 체크아웃일자
	public static Specification<ReservationEntity> equalsCheckoutDate(LocalDateTime reservationCheckoutDate) {
		LocalDate date = reservationCheckoutDate.toLocalDate();

		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCheckoutDate").as(LocalDate.class), date);
	}

	// 예약취소여부
	public static Specification<ReservationEntity> equalsReservationCancleStatus(Integer reservationCancelStatus) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCancelStatus"), reservationCancelStatus);
	}
}
