package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;

import java.time.LocalDateTime;
import java.util.Map;

public interface CouponIssueService {
    Map<String, Object> selectCouponIssueList(Integer pageNum, Integer couponIssueCodePk, String couponName,
        Integer customerCodePk, String customerName, Double couponDiscountRate, LocalDateTime couponIssueDate, LocalDateTime couponExpireDate,
        LocalDateTime couponUseDate, String orderBy, Integer sortBy);

    Map<String, Object> registCouponToCustomer(RequestCouponIssue requestCouponIssue);

}
