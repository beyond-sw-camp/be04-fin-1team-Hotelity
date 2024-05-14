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
								int pageNum,
								Integer customerCodeFk,
								LocalDateTime paymentDate,
								Integer paymentCancelStatus) {

		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Specification<PaymentEntity> spec = (root, query, criteriaBuilder) -> null;

		// 고객 이름별 조건
		if (customerCodeFk != null) {
			spec = spec.and(PaymentSpecification.equalsCustomerCodeFk(customerCodeFk));
		}
		// 결제 일자별 조건
		if (paymentDate != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentDate(paymentDate));
		}
		// 결제 취소 여부 별 조건
		if(paymentCancelStatus != null) {
			spec = spec.and(PaymentSpecification.equalsPaymentCancelStatus(paymentCancelStatus));
		}

		Page<PaymentEntity> paymentEntityPage = paymentRepository.findAll(spec, pageable);
		List<PaymentDTO> paymentDTOList = paymentEntityPage
			.stream()
			.map(paymentEntity -> mapper.map(paymentEntity, PaymentDTO.class))
			.toList();

		int totalPagesCount = paymentEntityPage.getTotalPages();
		int currentPageIndex = paymentEntityPage.getNumber();

		Map<String, Object> roomPageInfo = new HashMap<>();

		roomPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		roomPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		roomPageInfo.put(KEY_CONTENT, paymentDTOList);

		return roomPageInfo;
	}
}
