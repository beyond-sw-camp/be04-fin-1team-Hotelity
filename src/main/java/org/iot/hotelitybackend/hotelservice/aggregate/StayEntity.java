package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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

	// @Column(name = "reservation_code_fk")
	// private Integer reservationCodeFk;

	@ManyToOne
	@JoinColumn(name = "reservation_code_fk", referencedColumnName = "reservationCodePk")
	private ReservationEntity reservation;


	@Builder
	public StayEntity(
		Integer stayCodePk,
		LocalDateTime stayCheckinTime,
		LocalDateTime stayCheckoutTime,
		Integer stayPeopleCount,
		Integer employeeCodeFk,
		// Integer reservationCodeFk
		ReservationEntity reservation
	) {
		this.stayCodePk = stayCodePk;
		this.stayCheckinTime = stayCheckinTime;
		this.stayCheckoutTime = stayCheckoutTime;
		this.stayPeopleCount = stayPeopleCount;
		this.employeeCodeFk = employeeCodeFk;
		// this.reservationCodeFk = reservationCodeFk;
		this.reservation = reservation;
	}
}
