package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "coupon_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponCodePk;
    private String couponName;
    private String couponType;
    private double couponDiscountRate;
    private Date couponLaunchingDate;
    private String couponInfo;
    private int membershipLevelCodeFk;
}
