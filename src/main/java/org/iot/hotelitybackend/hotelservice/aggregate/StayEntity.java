package org.iot.hotelitybackend.hotelservice.aggregate;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stay_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StayEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stay_codePk;
	private Date stayCheckinTime;
	private Date stayCheckoutTime;
	private Integer stayPeopleAmount;
	private String employeeCodeFk;
	private Integer reservationCodeFk;

	@Builder
	public StayEntity(
		Integer stay_codePk,
		Date stayCheckinTime,
		Date stayCheckoutTime,
		Integer stayPeopleAmount,
		String employeeCodeFk,
		Integer reservationCodeFk
	) {
		this.stay_codePk = stay_codePk;
		this.stayCheckinTime = stayCheckinTime;
		this.stayCheckoutTime = stayCheckoutTime;
		this.stayPeopleAmount = stayPeopleAmount;
		this.employeeCodeFk = employeeCodeFk;
		this.reservationCodeFk = reservationCodeFk;
	}
}
