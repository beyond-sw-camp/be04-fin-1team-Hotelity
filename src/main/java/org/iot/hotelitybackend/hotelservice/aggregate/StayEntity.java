package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;

@Entity
@Table(name = "stay_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StayEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stayCodePk;

	private LocalDateTime stayCheckinTime;

	@Override
	public String toString() {
		return "StayEntity{" +
			"stayCodePk=" + stayCodePk +
			", stayCheckinTime=" + stayCheckinTime +
			", stayCheckoutTime=" + stayCheckoutTime +
			", stayPeopleCount=" + stayPeopleCount +
			", employeeCodeFk=" + employeeCodeFk +
			", reservationCodeFk=" + reservationCodeFk +
			'}';
	}

	private LocalDateTime stayCheckoutTime;
	private Integer stayPeopleCount;

	@Column(name = "employee_code_fk")
	private Integer employeeCodeFk;

	@Column(name = "reservation_code_fk")
	private Integer reservationCodeFk;

	@Builder
	public StayEntity(
		Integer stayCodePk,
		LocalDateTime stayCheckinTime,
		LocalDateTime stayCheckoutTime,
		Integer stayPeopleCount,
		Integer employeeCodeFk,
		Integer reservationCodeFk
	) {
		this.stayCodePk = stayCodePk;
		this.stayCheckinTime = stayCheckinTime;
		this.stayCheckoutTime = stayCheckoutTime;
		this.stayPeopleCount = stayPeopleCount;
		this.employeeCodeFk = employeeCodeFk;
		this.reservationCodeFk = reservationCodeFk;
	}

	@ManyToOne
	@JoinColumn(name = "employee_code_fk", insertable = false, updatable = false)
	private EmployeeEntity employee;

	@OneToOne
	@JoinColumn(name = "reservation_code_fk",	// JoinColum의 name은 root(Stay_tb) 테이블에서 설정한 컬럼명
		// referencedColumnName은 생략 가능(생략 시 자동으로 참조하는 테이블(reservation_tb)의 pk로 설정)
		referencedColumnName = "reservationCodePk",
		// insertable과 updatable은 reservationEntity와 reservationCodeFk를 StayEntity에 모두 담고자 할 때,
		// reservationCodeFk가 중복되므로 둘 중에 하나의 값만 db에 반영하도록 하는 설정
		insertable = false, updatable = false)
	private ReservationEntity reservation;
	//

	@Formula("(SELECT "
		+ "c.customer_name "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "JOIN customer_tb c ON (b.customer_code_fk = c.customer_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private String customerName;

	@Formula("(SELECT "
		+ "c.room_code_pk "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "JOIN room_tb c ON (b.room_code_fk = c.room_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private String roomCodeFk;

	@Formula("(SELECT "
		+ "c.room_number "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "JOIN room_tb c ON (b.room_code_fk = c.room_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private Integer roomNumber;

	@Formula("(SELECT "
		+ "d.room_name "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "JOIN room_tb c ON (b.room_code_fk = c.room_code_pk) "
		+ "JOIN room_category_tb d ON (c.room_category_code_fk = d.room_category_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private String roomName;

	@Formula("(SELECT "
		+ "e.room_level_name "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "JOIN room_tb c ON (b.room_code_fk = c.room_code_pk) "
		+ "JOIN room_category_tb d ON (c.room_category_code_fk = d.room_category_code_pk) "
		+ "JOIN room_level_tb e ON (d.room_level_code_fk = e.room_level_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private String roomLevelName;

	@Formula("(SELECT "
		+ "d.room_capacity "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "JOIN room_tb c ON (b.room_code_fk = c.room_code_pk) "
		+ "JOIN room_category_tb d ON (c.room_category_code_fk = d.room_category_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private Integer roomCapacity;

	@Formula("(SELECT "
		+ "b.branch_code_fk "
		+ "FROM stay_tb a "
		+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
		+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	)
	private String branchCodeFk;

	// @Formula("(SELECT "
	// 	+ "b.stay_period "
	// 	+ "FROM stay_tb a "
	// 	+ "JOIN reservation_tb b ON (a.reservation_code_fk = b.reservation_code_pk) "
	// 	+ "WHERE b.reservation_code_pk = reservation_code_fk)"
	// )
	// private String stayPeriod;
}
