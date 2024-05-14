package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;

import java.util.Map;

public interface CouponIssueService {
    Map<String, Object> selectCouponIssueList(int pageNum);

    Map<String, Object> registCouponToCustomer(RequestCouponIssue requestCouponIssue);

    Map<String, Object> selectCouponIssueListByCustomerCodeFk(int pageNum, Integer customerCodeFk);
}
