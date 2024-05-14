package org.iot.hotelitybackend.hotelservice.aggregate;

import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;

public class PaymentSpecification {

	public static Specification<PaymentDTO> equalsCustomerName(String customerName) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("customerName"), customerName);
	}

	public static Specification<PaymentDTO> equalsPaymentTypeName(String paymentTypeName) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("paymentTypeName"), paymentTypeName);
	}

	public static Specification<PaymentDTO> equalsPaymentCancleStatus(int paymentCancleStatus) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("paymentCancleStatus"), paymentCancleStatus);
	}
}
