package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

@Data
public class RequestCoupon {
    private String couponName;
    private String couponType;
    private float couponDiscountRate;
    private String couponInfo;
    private int membershipLevelCodeFk;
}
