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
@Table(name = "payment_log_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentCodePk;
	private Integer paymentAmount;
	private String paymentMethod;
	private Date paymentDate;
	private Integer paymentCancelStatus;
	private Integer reservationCodeFk;
	private Integer paymentTypeCodeFk;
	private Integer customerCodeFk;

	@Builder
	public PaymentEntity(
		Integer paymentCodePk,
		Integer paymentAmount,
		String paymentMethod,
		Date paymentDate,
		Integer paymentCancelStatus,
		Integer reservationCodeFk,
		Integer paymentTypeCodeFk,
		Integer customerCodeFk
	) {
		this.paymentCodePk = paymentCodePk;
		this.paymentAmount = paymentAmount;
		this.paymentMethod = paymentMethod;
		this.paymentDate = paymentDate;
		this.paymentCancelStatus = paymentCancelStatus;
		this.reservationCodeFk = reservationCodeFk;
		this.paymentTypeCodeFk = paymentTypeCodeFk;
		this.customerCodeFk = customerCodeFk;
	}
}
