package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.iot.hotelitybackend.hotelservice.repository.PaymentRepository;
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

	@Autowired
	public PaymentServiceImpl(PaymentRepository paymentRepository, ModelMapper mapper) {
		this.paymentRepository = paymentRepository;
		this.mapper = mapper;
	}

	@Override
	public Map<String, Object> selectPaymentLogList(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Page<PaymentEntity> paymentLogPage = paymentRepository.findAll(pageable);
		List<PaymentDTO> paymentDTOList =
			paymentLogPage.stream().map(paymentEntity -> mapper.map(paymentEntity, PaymentDTO.class)).toList();

		int totalPagesCount = paymentLogPage.getTotalPages();
		int currentPageIndex = paymentLogPage.getNumber();

		Map<String, Object> paymentLogPageInfo = new HashMap<>();

		paymentLogPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		paymentLogPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		paymentLogPageInfo.put(KEY_CONTENT, paymentDTOList);

		return paymentLogPageInfo;
	}
}
