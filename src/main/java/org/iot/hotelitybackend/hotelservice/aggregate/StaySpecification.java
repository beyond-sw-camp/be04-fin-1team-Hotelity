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

public class StaySpecification {

	private Join<StayEntity, ReservationEntity> reservationJoin;
	private Join<ReservationEntity, CustomerEntity> customerJoin;
	private Join<ReservationEntity, RoomEntity> roomJoin;
	private Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin;
	private Join<RoomCategoryEntity, RoomLevelEntity> roomLevelJoin;
	private Join<StayEntity, EmployeeEntity> employeeJoin;

	public void initializeJoins(Root<StayEntity> root) {
		this.reservationJoin = root.join("reservation");
		this.customerJoin = reservationJoin.join("customer");
		this.roomJoin = reservationJoin.join("room");
		this.roomCategoryJoin = roomJoin.join("roomCategory");
		this.roomLevelJoin = roomCategoryJoin.join("roomLevel");
		this.employeeJoin = root.join("employee");
	}

	// 투숙코드
	public static Specification<StayEntity> equalsStayCodePk(Integer stayCodePk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stayCodePk"), stayCodePk);
	}

	// 고객코드
	public static Specification<StayEntity> equalsCustomerCodeFk(Integer customerCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");

			return criteriaBuilder.equal(reservationJoin.get("customerCodeFk"), customerCodeFk);
		};
	}

	// 고객이름
	public static Specification<StayEntity> likeCustomerName(String customerName) {

		String pattern = "%" + customerName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, CustomerEntity> customerJoin = reservationJoin.join("customer");

			return criteriaBuilder.like(customerJoin.get("customerName"), pattern);
		};
	}

	// 객실코드
	public static Specification<StayEntity> equalsRoomCodeFk(String roomCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");

			return criteriaBuilder.equal(roomJoin.get("roomCodePk"), roomCodeFk);
		};
	}

	// 객실명
	public static Specification<StayEntity> likeRoomName(String roomName) {

		String pattern = "%" + roomName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");

			return criteriaBuilder.like(roomCategoryJoin.get("roomName"), pattern);
		};
	}

	// 객실 등급명
	public static Specification<StayEntity> likeRoomLevelName(String roomLevelName) {

		String pattern = "%" + roomLevelName + "%";

		return (root, query, CriteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
			Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
			Join<RoomCategoryEntity, RoomLevelEntity> roomLevelJoin = roomCategoryJoin.join("roomLevel");


			return CriteriaBuilder.like(roomLevelJoin.get("roomLevelName"), pattern);
		};
	}

	// 객실 수용 인원
	// public static Specification<StayEntity> equalsRoomCapacity(Integer roomCapacity) {
	// 	return (root, query, criteriaBuilder) -> {
	// 		Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
	// 		Join<ReservationEntity, RoomEntity> roomJoin = reservationJoin.join("room");
	// 		Join<RoomEntity, RoomCategoryEntity> roomCategoryJoin = roomJoin.join("roomCategory");
	//
	// 		return criteriaBuilder.equal(roomCategoryJoin.get("roomCapacity"), roomCapacity);
	// 	};
	// }

	// 투숙 인원
	public static Specification<StayEntity> equalsStayPeopleCount(Integer stayPeopleCount) {
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("stayPeopleCount"), stayPeopleCount);
	}

	// 지점코드
	public static Specification<StayEntity> equalsBranchCodeFk(String branchCodeFk) {
		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, ReservationEntity> reservationJoin = root.join("reservation");
			Join<ReservationEntity, BranchEntity> branchJoin = reservationJoin.join("branch");

			return criteriaBuilder.equal(branchJoin.get("branchCodePk"), branchCodeFk);
		};
	}

	// 체크인 날짜
	public static Specification<StayEntity> equalsStayCheckinTime(LocalDateTime stayCheckinTime) {

		LocalDate date = stayCheckinTime.toLocalDate();

		return (root, query, CriteriaBuilder) ->
			CriteriaBuilder.equal(root.get("stayCheckinTime").as(LocalDate.class), date);
	}

	// 체크아웃 날짜
	public static Specification<StayEntity> equalsStayCheckoutTime(LocalDateTime stayCheckoutTime) {

		LocalDate date = stayCheckoutTime.toLocalDate();

		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("stayCheckoutTime"), stayCheckoutTime);
	}

	// 직원코드
	public static Specification<StayEntity> equalsEmployeeCodeFk(Integer employeeCodeFk) {
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("employeeCodeFk"), employeeCodeFk);
	}

	// 직원이름
	public static Specification<StayEntity> likeEmployeeName(String employeeName) {

		String pattern = "%" + employeeName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<StayEntity, EmployeeEntity> employeeJoin = root.join("employee");

			return criteriaBuilder.like(employeeJoin.get("employeeName"), pattern);
		};
	}

	// 예약코드
	public static Specification<StayEntity> equalsReservationCodeFk(Integer reservationCodeFk) {
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("reservationCodeFk"), reservationCodeFk);
	}
}
