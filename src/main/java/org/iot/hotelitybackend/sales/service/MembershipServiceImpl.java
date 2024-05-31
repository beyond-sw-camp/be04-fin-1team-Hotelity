package org.iot.hotelitybackend.sales.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipSpecification;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MembershipServiceImpl implements MembershipService {

    private final ModelMapper mapper;
    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipServiceImpl(ModelMapper mapper, MembershipRepository membershipRepository) {
        this.mapper = mapper;
        this.membershipRepository = membershipRepository;
    }

    @Override
    public Map<String, Object> selectAllMembership(Integer pageNum, Integer membershipLevelCodePk,
        String membershipLevelName, String membershipInfo, Integer membershipCriteriaAmount) {

        Specification<MembershipEntity> specification = (root, query, criteriaBuilder) -> null;

        if(membershipLevelCodePk != null){
            specification = specification.and(MembershipSpecification.equalsMembershipLevelCodePk(membershipLevelCodePk));
        }
        if(membershipLevelName != null){
            specification = specification.and(MembershipSpecification.likesMembershipLevelName(membershipLevelName));
        }
        if(membershipInfo != null){
            specification = specification.and(MembershipSpecification.likesMembershipInfo(membershipInfo));
        }
        if(membershipCriteriaAmount != null){
            specification = specification.and(MembershipSpecification.equalsMembershipCriteriaAmount(membershipCriteriaAmount));
        }

        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("membershipLevelCodePk"));

        Map<String, Object> result = new HashMap<>();
        Page<MembershipEntity> membershipPage = membershipRepository.findAll(specification, pageable);

        List<MembershipDTO> membershipDTOList = membershipPage.stream()
            .map(membershipEntity -> mapper.map(membershipEntity, MembershipDTO.class))
            .collect(Collectors.toList());

        result.put(KEY_TOTAL_PAGES_COUNT, 1);
        result.put(KEY_CONTENT, membershipDTOList);

        return result;
    }
}
