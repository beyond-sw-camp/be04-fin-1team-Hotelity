package org.iot.hotelitybackend.hotelservice.aggregate;

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
@Table(name = "payment_type_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentTypeCodePk;
	private String paymentTypeName;

	@Builder
	public PaymentTypeEntity(Integer paymentTypeCodePk, String paymentTypeName) {
		this.paymentTypeCodePk = paymentTypeCodePk;
		this.paymentTypeName = paymentTypeName;
	}
}
