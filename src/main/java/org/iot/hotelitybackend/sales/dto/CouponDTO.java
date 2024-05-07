package org.iot.hotelitybackend.sales.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class CouponDTO {
    private int couponCodePk;
    private String couponName;
    private String couponType;
    private double couponDiscountRate;
    private Date couponLaunchingDate;
    private String couponInfo;
    private int membershipLevelCodeFk;
}
