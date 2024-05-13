package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.CouponDTO;

import java.util.Map;

public interface CouponService {
    Map<String, Object> selectAllCouponsType(int pageNum);

    CouponDTO selectCouponByCouponCodePk(int couponCodePk);
}
