package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;

public class PaymentSpecification {

	public static Specification<PaymentEntity> equalsCustomerCodeFk(Integer customerCodeFk) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
	}

	public static Specification<PaymentEntity> equalsPaymentDate(LocalDateTime paymentDate) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("paymentDate"), paymentDate);
	}

	public static Specification<PaymentEntity> equalsPaymentCancelStatus(Integer paymentCancelStatus) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("paymentCancelStatus"), paymentCancelStatus);
	}
}
