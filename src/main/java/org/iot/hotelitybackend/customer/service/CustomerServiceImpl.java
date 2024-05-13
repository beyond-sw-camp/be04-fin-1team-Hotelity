package org.iot.hotelitybackend.customer.service;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.dto.NationDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.customer.repository.NationRepository;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
import org.iot.hotelitybackend.sales.dto.MembershipIssueDTO;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
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
    private final NationRepository nationRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipIssueRepository membershipIssueRepository;


    @Autowired
    public CustomerServiceImpl(ModelMapper mapper, CustomerRepository customerRepository,
        NationRepository nationRepository,
        MembershipRepository membershipRepository, MembershipIssueRepository membershipIssueRepository) {
        this.mapper = mapper;
        this.customerRepository = customerRepository;
        this.nationRepository = nationRepository;
        this.membershipRepository = membershipRepository;
        this.membershipIssueRepository = membershipIssueRepository;
    }

    @Override
    public Map<String, Object> selectCustomersList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<CustomerEntity> customerPage = customerRepository.findAll(pageable);
        MembershipIssueDTO membershipIssueDTO = new MembershipIssueDTO();



        List<CustomerDTO> customerDTOList =
            customerPage.stream()
                .map(customerEntity -> mapper.map(customerEntity, CustomerDTO.class))
                .peek(customerDTO -> customerDTO.setNationName(mapper.map(nationRepository.findById(customerDTO.getNationCodeFk()), NationDTO.class).getNationName()))
                .peek(customerDTO -> customerDTO.setMembershipLevelName(membershipRepository.findById
                    (membershipIssueRepository.findByCustomerCodeFk(customerDTO.getCustomerCodePk()).getMembershipLevelCodeFk()).get().getMembershipLevelName()
                ))
                .toList();

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