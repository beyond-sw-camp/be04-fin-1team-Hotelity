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
@Table(name = "reservation_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reservationCodePk;
	private Date reservationDate;
	private Date reservationCheckinDate;
	private Date reservationCheckoutDate;
	private Integer reservationCancelStatus;
	private Integer reservationPersonnel;

	@Builder
	public ReservationEntity(
		Integer reservationCodePk,
		Date reservationDate,
		Date reservationCheckinDate,
		Date reservationCheckoutDate,
		Integer reservationCancelStatus,
		Integer reservationPersonnel
	) {
		this.reservationCodePk = reservationCodePk;
		this.reservationDate = reservationDate;
		this.reservationCheckinDate = reservationCheckinDate;
		this.reservationCheckoutDate = reservationCheckoutDate;
		this.reservationCancelStatus = reservationCancelStatus;
		this.reservationPersonnel = reservationPersonnel;
	}
}
