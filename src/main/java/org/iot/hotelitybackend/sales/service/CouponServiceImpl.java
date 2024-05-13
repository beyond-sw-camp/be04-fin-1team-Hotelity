package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.sales.aggregate.CouponEntity;
import org.iot.hotelitybackend.sales.aggregate.VocEntity;
import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.repository.CouponRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.iot.hotelitybackend.sales.vo.RequestCoupon;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final MembershipRepository membershipRepository;
    @Autowired
    public CouponServiceImpl(ModelMapper mapper, CouponRepository couponRepository, MembershipRepository membershipRepository) {
        this.mapper = mapper;
        this.couponRepository = couponRepository;
        this.membershipRepository = membershipRepository;
    }

    @Override
    public Map<String, Object> selectAllCouponsType(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<CouponEntity> couponPage = couponRepository.findAll(pageable);
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
    public CouponDTO selectCouponByCouponCodePk(int couponCodePk) {
        CouponEntity couponEntity = couponRepository.findById(couponCodePk)
                .orElseThrow(IllegalArgumentException::new);

        String membershipLevelName = membershipRepository
                .findById(couponEntity.getMembershipLevelCodeFk())
                .get()
                .getMembershipLevelName();

        CouponDTO couponDTO = mapper.map(couponEntity, CouponDTO.class);

        couponDTO.setMembershipLevelName(membershipLevelName);

        return couponDTO;
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
}
