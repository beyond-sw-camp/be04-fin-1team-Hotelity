package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseCoupon {
    private Integer couponCodePk;
    private String couponName;
    private String couponType;
    private Double couponDiscountRate;
    private Date couponLaunchingDate;
    private String couponInfo;
    private Integer membershipLevelCodeFk;
}
