package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.sales.dto.MembershipIssueDTO;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipIssueServiceImpl implements MembershipIssueService{

    private final ModelMapper mapper;
    private final MembershipIssueRepository membershipIssueRepository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipIssueServiceImpl(ModelMapper mapper, MembershipIssueRepository membershipIssueRepository, CustomerRepository customerRepository, MembershipRepository membershipRepository) {
        this.mapper = mapper;
        this.membershipIssueRepository = membershipIssueRepository;
        this.customerRepository = customerRepository;
        this.membershipRepository = membershipRepository;
    }

    @Override
    public MembershipIssueDTO selectAllMembershipIssueList(int membershipIssueCodePk) {
        MembershipIssueEntity membershipIssueEntity = membershipIssueRepository.findById(membershipIssueCodePk)
                .orElseThrow(IllegalArgumentException::new);

        String customerName = customerRepository
                .findById(membershipIssueEntity.getCustomerCodeFk())
                .get()
                .getCustomerName();

        String membershipLevelName = membershipRepository
                .findById(membershipIssueEntity.getMembershipLevelCodeFk())
                .get()
                .getMembershipLevelName();

        MembershipIssueDTO membershipIssueDTO = mapper.map(membershipIssueEntity, MembershipIssueDTO.class);

        membershipIssueDTO.setCustomerName(customerName);
        membershipIssueDTO.setMembershipLevelName(membershipLevelName);

        return membershipIssueDTO;
    }
}
