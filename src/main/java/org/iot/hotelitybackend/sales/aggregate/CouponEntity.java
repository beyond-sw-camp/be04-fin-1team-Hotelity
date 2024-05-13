package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Integer couponCodePk;
    private String couponName;
    private String couponType;
    private Double couponDiscountRate;
    private Date couponLaunchingDate;
    private String couponInfo;
    private Integer membershipLevelCodeFk;

    @Builder
    public CouponEntity(
            Integer couponCodePk,
            String couponName,
            String couponType,
            double couponDiscountRate,
            Date couponLaunchingDate,
            String couponInfo,
            Integer membershipLevelCodeFk
    ) {
        this.couponCodePk = couponCodePk;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponDiscountRate = couponDiscountRate;
        this.couponLaunchingDate = couponLaunchingDate;
        this.couponInfo = couponInfo;
        this.membershipLevelCodeFk = membershipLevelCodeFk;
    }
}
