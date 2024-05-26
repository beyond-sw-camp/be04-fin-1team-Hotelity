package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.sales.aggregate.CouponIssueEntity;
import org.iot.hotelitybackend.sales.aggregate.CouponIssueSpecification;
import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.dto.CouponIssueDTO;
import org.iot.hotelitybackend.sales.repository.CouponIssueRepository;
import org.iot.hotelitybackend.sales.repository.CouponRepository;
import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
    public Map<String, Object> selectCouponIssueList(Integer pageNum, Integer couponIssueCodePk, String couponName,
        Integer customerCodePk, String customerName, Double couponDiscountRate, LocalDateTime couponIssueDate, LocalDateTime couponExpireDate,
        LocalDateTime couponUseDate, String orderBy, Integer sortBy) {
        Pageable pageable;
        if(orderBy == null){
            pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("couponIssueCodePk"));
        } else{
            if(sortBy == 1){
                pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
            } else {
                pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
            }
        }

        Specification<CouponIssueEntity> specification = (root, query, criteriaBuilder) -> null;

        if(couponIssueCodePk != null){
            specification = specification.and(CouponIssueSpecification.equalsCouponIssueCodePk(couponIssueCodePk));
        }
        if(couponName != null){
            specification = specification.and(CouponIssueSpecification.likeCouponName(couponName));
        }
        if(customerName != null){
            specification = specification.and(CouponIssueSpecification.likeCustomerName(customerName));
        }
        if(couponDiscountRate != null){
            specification = specification.and(CouponIssueSpecification.equalsCouponDiscountRate(couponDiscountRate));
        }
        if(couponIssueDate != null){
            specification = specification.and(CouponIssueSpecification.equalsCouponIssueDate(couponIssueDate));
        }
        if(couponExpireDate != null){
            specification = specification.and(CouponIssueSpecification.equalsCouponExpireDate(couponExpireDate));
        }
        if(couponUseDate != null){
            specification = specification.and(CouponIssueSpecification.equalsCouponUseDate(couponUseDate));
        }
        if(customerCodePk != null){
            specification = specification.and(CouponIssueSpecification.equalsCustomerCodePk(customerCodePk));
        }

        Page<CouponIssueEntity> couponIssuePage = couponIssueRepository.findAll(specification, pageable);
        List<CouponIssueDTO> couponIssueDTOList = couponIssuePage
            .stream()
            .map(couponIssueEntity -> mapper.map(couponIssueEntity, CouponIssueDTO.class))
            .peek(couponIssueDTO -> {
                    couponIssueDTO.setCustomerName(
                        mapper.map(customerRepository.findById(couponIssueDTO.getCustomerCodeFk()), CustomerDTO.class)
                            .getCustomerName());
                    couponIssueDTO.setCouponName(couponRepository.findById(couponIssueDTO.getCouponCodeFk()).get().getCouponName());
                    couponIssueDTO.setCouponDiscountRate(couponRepository.findById(couponIssueDTO.getCouponCodeFk()).get()
                        .getCouponDiscountRate());
                }
            )
            .toList();

        int totalPagesCount = couponIssuePage.getTotalPages();
        int currentPageIndex = couponIssuePage.getNumber();

        Map<String, Object> couponIssuePageInfo = new HashMap<>();

        couponIssuePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        couponIssuePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        couponIssuePageInfo.put(KEY_CONTENT, couponIssueDTOList);

        return couponIssuePageInfo;
    }

    @Override
    public Map<String, Object> registCouponToCustomer(RequestCouponIssue requestCouponIssue) {

        LocalDate requestIssueDate = LocalDate.now();
        LocalDate requestExpireDate = requestIssueDate.plusMonths(3);

        Date couponIssueDate = Date.from(requestIssueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date couponExpireDate = Date.from(requestExpireDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        CouponIssueEntity couponIssueEntity = CouponIssueEntity.builder()
                .couponIssueDate(couponIssueDate)
                .customerCodeFk(requestCouponIssue.getCustomerCodeFk())
                .couponCodeFk(requestCouponIssue.getCouponCodeFk())
                .couponExpireDate(couponExpireDate)
                .couponIssueBarcode(generateBarcode())
                .build();

        Map<String, Object> registCouponIssueInfo = new HashMap<>();

        registCouponIssueInfo.put(KEY_CONTENT, mapper.map(couponIssueRepository.save(couponIssueEntity), CouponIssueDTO.class));

        return registCouponIssueInfo;
    }

    public String generateBarcode() {
        Random random = new Random();
        String prefix = "8803010"; // 880은 국가코드(한국), 3010은 제조업체코드(현재는 제조업체가 없으므로 휴대폰번호 뒷자리로 임시 사용)
        StringBuilder barcode = new StringBuilder(prefix);
        for (int i  = 0; i < 5; i++) {
            barcode.append(random.nextInt(10));
        }

        barcode.append(calculateCheckDigit(barcode.toString()));

        return barcode.toString();
    }

    public int calculateCheckDigit(String barcode) {
        int sum = 0;

        for (int i = 0; i < barcode.length(); i++) {
            int digit = Integer.parseInt(barcode.substring(i, i + 1));
            sum += (i % 2 == 0) ? digit*1 : digit*3;
        }

        int checkDigit = 10 - (sum%10);
        if (checkDigit == 10) checkDigit = 0;
        return checkDigit;
    }
}
