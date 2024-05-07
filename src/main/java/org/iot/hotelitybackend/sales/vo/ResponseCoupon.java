package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseCoupon {
    private int couponCodePk;
    private String couponName;
    private String couponType;
    private double couponDiscountRate;
    private Date couponLaunchingDate;
    private String couponInfo;
    private int membershipLevelCodeFk;
}
