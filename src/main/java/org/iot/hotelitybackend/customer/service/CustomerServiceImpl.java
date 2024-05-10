package org.iot.hotelitybackend.customer.service;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.dto.PaymentDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final ModelMapper mapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(ModelMapper mapper, CustomerRepository customerRepository) {
        this.mapper = mapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public Map<String, Object> selectCustomersList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<CustomerEntity> customerPage = customerRepository.findAll(pageable);
        List<PaymentDTO> customerDTOList =
                customerPage.stream().map(customerEntity -> mapper.map(customerEntity, PaymentDTO.class)).toList();

        int totalPagesCount = customerPage.getTotalPages();
        int currentPageIndex = customerPage.getNumber();

        Map<String, Object> customerPageInfo = new HashMap<>();

        customerPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        customerPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        customerPageInfo.put(KEY_CONTENT, customerDTOList);

        return customerPageInfo;
    }

    @Override
    public CustomerDTO selectCustomerByCustomerCodePk(int customerCodePk) {

        CustomerEntity customerEntity = customerRepository.findById(customerCodePk).get();
        return mapper.map(customerEntity, CustomerDTO.class);
    }
}
