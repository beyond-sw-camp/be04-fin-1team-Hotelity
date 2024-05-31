package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentSpecification;
import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.repository.PaymentRepository;
import org.iot.hotelitybackend.hotelservice.repository.PaymentTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
	private final PaymentRepository paymentRepository;
	private final ModelMapper mapper;
	private final PaymentTypeRepository paymentTypeRepository;
	private final CustomerRepository customerRepository;

	@Autowired
	public PaymentServiceImpl(PaymentRepository paymentRepository, ModelMapper mapper,
		PaymentTypeRepository paymentTypeRepository, CustomerRepository customerRepository) {
		this.paymentRepository = paymentRepository;
		this.mapper = mapper;
		this.paymentTypeRepository = paymentTypeRepository;
		this.customerRepository = customerRepository;
	}

	/* 다중 조건 검색을 적용한 결제 내역 전체 조회 */
	@Override
	public Map<String, Object> selectPaymentLogList(
		Integer pageNum,
		Integer customerCodeFk, String customerName,
		LocalDateTime paymentDate, Integer paymentCancelStatus,
		String paymentMethod, Integer reservationCodeFk,
		Integer paymentTypeCodeFk, String paymentTypeName,
		String orderBy, Integer sortBy) {



		Specification<PaymentEntity> spec = (root, query, criteriaBuilder) -> null;

		// 고객 코드
		if (customerCodeFk != null) {
			spec = spec.and(PaymentSpecification.equalsCustomerCodeFk(customerCodeFk));
		}

		// 고객 이름별 조건
		if (customerName != null) {
			spec = spec.and(PaymentSpecification.equalsCustomerName(customerName));
		}

		// 결제 일자별 조건
		if (paymentDate != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentDate(paymentDate));
		}

		// 결제 취소 여부 별 조건
		if (paymentCancelStatus != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentCancelStatus(paymentCancelStatus));
		}

		// 결제 수단
		if (paymentMethod != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentMethod(paymentMethod));
		}

		// 예약 코드
		if (reservationCodeFk != null) {
			spec = spec.and(PaymentSpecification.equalsReservationCodeFk(reservationCodeFk));
		}

		// 결제 종류 코드
		if (paymentTypeCodeFk != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentTypeCodeFk(paymentTypeCodeFk));
		}

		// 결제 종류 이름
		if (paymentTypeName != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentTypeName(paymentTypeName));
		}

		Map<String, Object> roomPageInfo = new HashMap<>();
		List<PaymentDTO> paymentDTOList;

		// 1. 페이징 처리 안할 때
		if (pageNum != null) {
			Pageable pageable;

			if (orderBy == null) {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("paymentCodePk"));
			} else {
				if (sortBy == 1) {
					pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
				}
				else{
					pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
				}
			}
			Page<PaymentEntity> paymentEntityPage = paymentRepository.findAll(spec, pageable);
			paymentDTOList = paymentEntityPage
				.stream()
				.map(paymentEntity -> mapper.map(paymentEntity, PaymentDTO.class))
				.toList();

			int totalPagesCount = paymentEntityPage.getTotalPages();
			int currentPageIndex = paymentEntityPage.getNumber();

			roomPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
			roomPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);

		// 2. 페이징 처리 안할 때
		} else {
			List<PaymentEntity> paymentEntityList = paymentRepository.findAll(spec);
			paymentDTOList = paymentEntityList
				.stream()
				.map(paymentEntity -> mapper.map(paymentEntity, PaymentDTO.class))
				.toList();
		}
		roomPageInfo.put(KEY_CONTENT, paymentDTOList);

		return roomPageInfo;
	}
}
