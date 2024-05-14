package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.vo.RequestCoupon;

import java.util.Map;

public interface CouponService {
    Map<String, Object> selectAllCouponsType(int pageNum);

    CouponDTO selectCouponByCouponCodePk(int couponCodePk);

    Map<String, Object> registCoupon(RequestCoupon requestCoupon);

    Map<String, Object> modifyCoupon(RequestCoupon requestCoupon, int couponCodePk);

    Map<String, Object> selectSearchedCouponsList(int pageNum, Float couponDiscountRate, String couponType);
}
