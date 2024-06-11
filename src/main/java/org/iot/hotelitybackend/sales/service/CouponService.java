package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.vo.CouponSearchCriteria;
import org.iot.hotelitybackend.sales.vo.RequestCoupon;

import java.util.Date;
import java.util.Map;

public interface CouponService {
    Map<String, Object> selectAllCouponsType(CouponSearchCriteria criteria);

    Map<String, Object> registCoupon(RequestCoupon requestCoupon);

    Map<String, Object> modifyCoupon(RequestCoupon requestCoupon, int couponCodePk);

}
