package org.iot.hotelitybackend.sales.service;

import java.util.Map;

public interface CouponIssueService {
    Map<String, Object> selectCouponIssueList(int pageNum);
}
