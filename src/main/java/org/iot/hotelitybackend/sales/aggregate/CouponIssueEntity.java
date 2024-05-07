package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "coupon_issue_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponIssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponIssueCodePk;
    private int customerCodeFk;
    private int couponCodeFk;
    private String couponIssueBarcode;
    private Date couponIssueDate;
    private Date couponExpireDate;
    private Date couponUseDate;
}
