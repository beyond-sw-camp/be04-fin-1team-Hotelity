package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
