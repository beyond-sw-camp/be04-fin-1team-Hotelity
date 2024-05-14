package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

@Data
public class RequestCouponIssue {
    private int couponCodeFk;
    private int customerCodeFk;
}
