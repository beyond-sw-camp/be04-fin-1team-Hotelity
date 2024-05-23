package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.aggregate.CouponEntity;
import org.iot.hotelitybackend.sales.aggregate.CouponSpecification;
import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.repository.CouponRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.iot.hotelitybackend.sales.vo.RequestCoupon;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class CouponServiceImpl implements CouponService{

    private final ModelMapper mapper;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponServiceImpl(ModelMapper mapper, CouponRepository couponRepository) {
        this.mapper = mapper;
        this.couponRepository = couponRepository;
    }

    @Override
    public Map<String, Object> selectAllCouponsType(Integer pageNum, Integer couponCodePk, String couponName,
        String couponType, Double couponDiscountRate, Date couponLaunchingDate, String couponInfo,
        Integer membershipLevelCodeFk, String orderBy, Integer sortBy) {
        Pageable pageable;
        if(orderBy == null){
            pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("couponCodePk"));
        } else{
            if(sortBy == 1){
                pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
            } else {
                pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
            }
        }

        Specification<CouponEntity> specification = (root, query, criteriaBuilder) -> null;

        if(couponCodePk != null){
            specification = specification.and(CouponSpecification.equalsCouponCodePk(couponCodePk));
        }
        if(couponName != null){
            specification = specification.and(CouponSpecification.likesCouponName(couponName));
        }
        if(couponType != null){
            specification = specification.and(CouponSpecification.likesCouponType(couponType));
        }
        if(couponDiscountRate != null){
            specification = specification.and(CouponSpecification.equalsCouponDiscountRate(couponDiscountRate));
        }
        if(couponLaunchingDate != null){
            specification = specification.and(CouponSpecification.equalsCouponLaunchingDate(couponLaunchingDate));
        }
        if(couponInfo != null){
            specification = specification.and(CouponSpecification.likesCouponInfo(couponInfo));
        }

        Page<CouponEntity> couponPage = couponRepository.findAll(specification, pageable);
        List<CouponDTO> couponDTOList = couponPage.stream().map(couponEntity -> mapper.map(couponEntity, CouponDTO.class)).toList();

        int totalPagesCount = couponPage.getTotalPages();
        int currentPageIndex = couponPage.getNumber();

        Map<String, Object> couponPageInfo = new HashMap<>();

        couponPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        couponPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        couponPageInfo.put(KEY_CONTENT, couponDTOList);

        return couponPageInfo;
    }

    @Override
    public Map<String, Object> registCoupon(RequestCoupon requestCoupon) {
        CouponEntity couponEntity = CouponEntity.builder()
                .couponName(requestCoupon.getCouponName())
                .couponType(requestCoupon.getCouponType())
                .couponDiscountRate(requestCoupon.getCouponDiscountRate())
                .couponInfo(requestCoupon.getCouponInfo())
                .membershipLevelCodeFk(requestCoupon.getMembershipLevelCodeFk())
                .couponLaunchingDate(new Date())
                .build();

        Map<String, Object> registCouponInfo = new HashMap<>();

        registCouponInfo.put(KEY_CONTENT, mapper.map(couponRepository.save(couponEntity), CouponDTO.class));

        return registCouponInfo;
    }

    @Override
    public Map<String, Object> modifyCoupon(RequestCoupon requestCoupon, int couponCodePk) {
        CouponEntity couponEntity = CouponEntity.builder()
                .couponCodePk(couponCodePk)
                .couponName(requestCoupon.getCouponName())
                .couponType(requestCoupon.getCouponType())
                .couponDiscountRate(requestCoupon.getCouponDiscountRate())
                .couponInfo(requestCoupon.getCouponInfo())
                .couponLaunchingDate(couponRepository.findById(couponCodePk).get().getCouponLaunchingDate())
                .membershipLevelCodeFk(requestCoupon.getMembershipLevelCodeFk())
                .build();

        Map<String, Object> modifyCouponInfo = new HashMap<>();

        modifyCouponInfo.put(KEY_CONTENT, mapper.map(couponRepository.save(couponEntity), CouponDTO.class));

        return modifyCouponInfo;
    }
}
