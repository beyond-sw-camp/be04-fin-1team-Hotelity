package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.vo.CouponIssueSearchCriteria;
import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;

import java.time.LocalDateTime;
import java.util.Map;

public interface CouponIssueService {
    Map<String, Object> selectCouponIssueList(
        CouponIssueSearchCriteria criteria
    );

    Map<String, Object> registCouponToCustomer(RequestCouponIssue requestCouponIssue);

}
