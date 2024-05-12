package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.dto.PaymentTypeDTO;
import org.iot.hotelitybackend.hotelservice.repository.PaymentRepository;
import org.iot.hotelitybackend.hotelservice.repository.PaymentTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	/* 전체 결제 내역 조회 */
	@Override
	public Map<String, Object> selectPaymentLogList(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Page<PaymentEntity> paymentLogPage = paymentRepository.findAll(pageable);
		List<PaymentDTO> paymentDTOList =
			paymentLogPage.stream().map(paymentEntity -> mapper.map(paymentEntity, PaymentDTO.class))
				.peek(paymentDTO -> paymentDTO.setPaymentTypeName(
					mapper.map(paymentTypeRepository.findById(paymentDTO.getPaymentTypeCodeFk()), PaymentTypeDTO.class).getPaymentTypeName()))
				.peek(paymentDTO -> paymentDTO.setCustomerName(
					mapper.map(customerRepository.findById(paymentDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()))
				.toList();

		int totalPagesCount = paymentLogPage.getTotalPages();
		int currentPageIndex = paymentLogPage.getNumber();

		Map<String, Object> paymentLogPageInfo = new HashMap<>();

		paymentLogPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		paymentLogPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		paymentLogPageInfo.put(KEY_CONTENT, paymentDTOList);

		return paymentLogPageInfo;
	}

	/* 다중 조건 검색을 적용한 결제 내역 전체 조회 */
	

	/* 날짜별 결제 내역 조회 */
	@Override
	public Map<String, Object> selectPaymentByPaymentDate(Date paymentDate) {

		return null;
	}

	/* 지점별 결제 내역 조회 */

	/* 결제 종류별 결제 내역 조회 */

}
