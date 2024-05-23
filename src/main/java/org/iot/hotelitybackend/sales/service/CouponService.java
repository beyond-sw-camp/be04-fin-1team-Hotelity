package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.vo.RequestCoupon;

import java.util.Date;
import java.util.Map;

public interface CouponService {
    Map<String, Object> selectAllCouponsType(Integer pageNum, Integer couponCodePk, String couponName, String couponType,
		Double couponDiscountRate, Date couponLaunchingDate, String couponInfo, Integer membershipLevelCodeFk,
        String orderBy, Integer sortBy);

    Map<String, Object> registCoupon(RequestCoupon requestCoupon);

    Map<String, Object> modifyCoupon(RequestCoupon requestCoupon, int couponCodePk);

}
