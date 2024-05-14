package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;


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
