package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
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
import java.util.stream.Collectors;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class MembershipServiceImpl implements MembershipService {

    private final ModelMapper mapper;
    private final MembershipRepository membershipRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public MembershipServiceImpl(ModelMapper mapper, MembershipRepository membershipRepository, CustomerRepository customerRepository) {
        this.mapper = mapper;
        this.membershipRepository = membershipRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Map<String, Object> selectAllMembership(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<MembershipEntity> membershipPage = membershipRepository.findAll(pageable);
        List<MembershipDTO> membershipDTOList = membershipPage
                .stream()
                .map(membershipEntity -> mapper.map(membershipEntity, MembershipDTO.class))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put(KEY_CONTENT, membershipDTOList);
        result.put(KEY_TOTAL_PAGES_COUNT, membershipPage.getTotalPages());
        result.put(KEY_CURRENT_PAGE_INDEX, membershipPage.getNumber());

        return result;
    }
}
