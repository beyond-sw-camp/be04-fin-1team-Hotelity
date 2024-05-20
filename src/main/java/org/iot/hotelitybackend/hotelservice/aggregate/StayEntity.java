package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "stay_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StayEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stayCodePk;

	private LocalDateTime stayCheckinTime;
	private LocalDateTime stayCheckoutTime;
	private Integer stayPeopleCount;

	@Column(name = "employee_code_fk")
	private Integer employeeCodeFk;

	@Column(name = "reservation_code_fk")
	private Integer reservationCodeFk;

	@OneToOne
	@JoinColumn(name = "reservation_code_fk",	// JoinColum의 name은 root(Stay_tb) 테이블에서 설정한 컬럼명
		// referencedColumnName은 생략 가능(생략 시 자동으로 참조하는 테이블(reservation_tb)의 pk로 설정)
		referencedColumnName = "reservationCodePk",
		// insertable과 updatable은 reservationEntity와 reservationCodeFk를 StayEntity에 모두 담고자 할 때,
		// reservationCodeFk가 중복되므로 둘 중에 하나의 값만 db에 반영하도록 하는 설정
		insertable = false, updatable = false)
	private ReservationEntity reservation;

	@Builder
	public StayEntity(
		Integer stayCodePk,
		LocalDateTime stayCheckinTime,
		LocalDateTime stayCheckoutTime,
		Integer stayPeopleCount,
		Integer employeeCodeFk,
		Integer reservationCodeFk,
		ReservationEntity reservation
	) {
		this.stayCodePk = stayCodePk;
		this.stayCheckinTime = stayCheckinTime;
		this.stayCheckoutTime = stayCheckoutTime;
		this.stayPeopleCount = stayPeopleCount;
		this.employeeCodeFk = employeeCodeFk;
		this.reservationCodeFk = reservationCodeFk;
		this.reservation = reservation;
	}
}
