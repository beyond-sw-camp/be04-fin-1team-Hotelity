package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

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

    @Formula(
            "( SELECT m.membership_level_name " +
                    "FROM coupon_tb c " +
                    "JOIN membership_tb m ON m.membership_level_code_pk = c.membership_level_code_fk " +
                    "WHERE c.coupon_code_pk = coupon_code_pk " +
                    ")"
    )
    private String membershipLevelName;

    @Builder
    public CouponEntity(
            Integer couponCodePk,
            String couponName,
            String couponType,
            double couponDiscountRate,
            Date couponLaunchingDate,
            String couponInfo,
            Integer membershipLevelCodeFk, String membershipLevelName
    ) {
        this.couponCodePk = couponCodePk;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponDiscountRate = couponDiscountRate;
        this.couponLaunchingDate = couponLaunchingDate;
        this.couponInfo = couponInfo;
        this.membershipLevelCodeFk = membershipLevelCodeFk;
        this.membershipLevelName = membershipLevelName;
    }
}
