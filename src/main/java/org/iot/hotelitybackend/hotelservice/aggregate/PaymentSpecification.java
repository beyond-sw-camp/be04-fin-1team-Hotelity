package org.iot.hotelitybackend.hotelservice.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class PaymentSpecification {

	// 고객 코드
	public static Specification<PaymentEntity> equalsCustomerCodeFk(Integer customerCodeFk) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerCodeFk"), customerCodeFk);
	}

	// 고객 이름
	public static Specification<PaymentEntity> equalsCustomerName(String customerName) {

		String pattern = "%" + customerName + "%";

		return (root, query, criteriaBuilder) -> {
			Join<PaymentEntity, CustomerEntity> customerJoin = root.join("customer");

			return criteriaBuilder.like(customerJoin.get("customerName"), pattern);
		};
	}

	// 결제 수단
	public static Specification<PaymentEntity> equalsPaymentMethod(String paymentMethod) {
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod));
	}

	// 예약 코드
	public static Specification<PaymentEntity> equalsReservationCodeFk(Integer reservationCodeFk) {
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reservationCodeFk"), reservationCodeFk));
	}

	// 결제 종류 코드
	public static Specification<PaymentEntity> equalsPaymentTypeCodeFk(Integer paymentTypeCodeFk) {
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentTypeCodeFk"), paymentTypeCodeFk));
	}

	// 결제 종류 이름
	public static Specification<PaymentEntity> equalsPaymentTypeName(String paymentTypeName) {
		return (root, query, criteriaBuilder) -> {
			Join<PaymentEntity, PaymentTypeEntity> paymentTypeJoin = root.join("paymentType");

			return criteriaBuilder.equal(paymentTypeJoin.get("paymentTypeName"), paymentTypeName);
		};
	}

	// 결제 일자
	public static Specification<PaymentEntity> equalsPaymentDate(LocalDateTime paymentDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentDate"), paymentDate);
	}

	// 결제 취소 여부
	public static Specification<PaymentEntity> equalsPaymentCancelStatus(Integer paymentCancelStatus) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentCancelStatus"),
			paymentCancelStatus);
	}
}
