package org.iot.hotelitybackend.hotelmanagement.aggregate;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomEntity {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_code_pk")
	private String roomCodePk;
	private String branchCodeFk;
	private Integer roomNumber;
	@Column(name = "room_category_code_fk")
	private Integer roomCategoryCodeFk;
	private String  roomCurrentStatus;
	private Float roomDiscountRate;
	private String roomImageLink;
	private String roomView;
	@Formula(
		"("
			+ "SELECT rc.room_name "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private String roomName;
	@Formula(
		"("
			+ "SELECT rc.room_sub_rooms_count "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private String roomSubRoomsCount;
	@Formula(
		"("
			+ "SELECT rc.room_price "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private String roomPrice;
	@Formula(
		"("
			+ "SELECT rc.room_capacity "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private Integer roomCapacity;
	@Formula(
		"("
			+ "SELECT rc.room_bathroom_count "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private Integer roomBathroomCount;
	@Formula(
		"("
			+ "SELECT rc.room_specific_info "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private String roomSpecificInfo;
	@Formula(
		"("
			+ "SELECT rl.room_level_name "
			+ "FROM room_tb r "
			+ "JOIN room_category_tb rc ON rc.room_category_code_pk = r.room_category_code_fk "
			+ "JOIN room_level_tb rl ON rl.room_level_code_pk = rc.room_level_code_fk "
			+ "WHERE r.room_code_pk = room_code_pk"
			+ ")"
	)
	private String roomLevelName;

	@Builder
	public RoomEntity(String roomCodePk, String branchCodeFk, Integer roomNumber, Integer roomCategoryCodeFk,
		String roomCurrentStatus, Float roomDiscountRate, String roomImageLink, String roomView, String roomName,
		String roomSubRoomsCount, String roomPrice, Integer roomCapacity, Integer roomBathroomCount,
		String roomSpecificInfo, String roomLevelName) {
		this.roomCodePk = roomCodePk;
		this.branchCodeFk = branchCodeFk;
		this.roomNumber = roomNumber;
		this.roomCategoryCodeFk = roomCategoryCodeFk;
		this.roomCurrentStatus = roomCurrentStatus;
		this.roomDiscountRate = roomDiscountRate;
		this.roomImageLink = roomImageLink;
		this.roomView = roomView;
		this.roomName = roomName;
		this.roomSubRoomsCount = roomSubRoomsCount;
		this.roomPrice = roomPrice;
		this.roomCapacity = roomCapacity;
		this.roomBathroomCount = roomBathroomCount;
		this.roomSpecificInfo = roomSpecificInfo;
		this.roomLevelName = roomLevelName;
	}

	/* StaySpecification 검색 조건에 사용 */
	@ManyToOne
	@JoinColumn(name = "room_category_code_fk", insertable = false, updatable = false)
	private RoomCategoryEntity roomCategory;
}
