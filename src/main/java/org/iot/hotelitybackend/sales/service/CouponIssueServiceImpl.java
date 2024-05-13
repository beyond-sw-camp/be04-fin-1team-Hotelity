package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.sales.aggregate.CouponIssueEntity;
import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.dto.CouponIssueDTO;
import org.iot.hotelitybackend.sales.repository.CouponIssueRepository;
import org.iot.hotelitybackend.sales.repository.CouponRepository;
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
public class CouponIssueServiceImpl implements CouponIssueService{

    private final ModelMapper mapper;
    private final CouponIssueRepository couponIssueRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponIssueServiceImpl(ModelMapper mapper, CouponIssueRepository couponIssueRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
        this.mapper = mapper;
        this.couponIssueRepository = couponIssueRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public Map<String, Object> selectCouponIssueList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<CouponIssueEntity> couponIssuePage = couponIssueRepository.findAll(pageable);
        List<CouponIssueDTO> couponIssueDTOList = couponIssuePage.stream().map(couponIssueEntity -> mapper.map(couponIssueEntity, CouponIssueDTO.class))
                .peek(couponIssueDTO -> couponIssueDTO.setCustomerName(
                        mapper.map(customerRepository.findById(couponIssueDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()
                ))
                .peek(couponIssueDTO -> couponIssueDTO.setMembershipLevelName(
                        mapper.map(couponRepository.findById(couponIssueDTO.getCouponCodeFk()), CouponDTO.class).getCouponName()
                ))
                .toList();

        int totalPagesCount = couponIssuePage.getTotalPages();
        int currentPageIndex = couponIssuePage.getNumber();

        Map<String, Object> couponIssuePageInfo = new HashMap<>();

        couponIssuePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        couponIssuePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        couponIssuePageInfo.put(KEY_CONTENT, couponIssueDTOList);

        return couponIssuePageInfo;
    }
}
