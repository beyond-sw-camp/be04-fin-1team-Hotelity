package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "coupon_issue_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponIssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponIssueCodePk;
    private Integer customerCodeFk;
    private Integer couponCodeFk;
    private String couponIssueBarcode;
    private Date couponIssueDate;
    private Date couponExpireDate;
    private Date couponUseDate;

    @Formula("("
        + "SELECT c.coupon_name "
        + "FROM coupon_issue_tb ci "
        + "JOIN coupon_tb c ON ci.coupon_code_fk = c.coupon_code_pk "
        + "WHERE ci.coupon_issue_code_pk = coupon_issue_code_pk "
        + ")")
    private String couponName;

    @Formula("("
        + "SELECT cu.customer_name "
        + "FROM coupon_issue_tb ci "
        + "JOIN customer_tb cu ON ci.customer_code_fk = cu.customer_code_pk "
        + "WHERE ci.coupon_issue_code_pk = coupon_issue_code_pk "
        + ")")
    private String customerName;

    @Formula("("
        + "SELECT c.coupon_discount_rate "
        + "FROM coupon_issue_tb ci "
        + "JOIN coupon_tb c ON ci.coupon_code_fk = c.coupon_code_pk "
        + "WHERE ci.coupon_issue_code_pk = coupon_issue_code_pk "
        + ")")
    private Double couponDiscountRate;

    @Builder
    public CouponIssueEntity(
        Integer couponIssueCodePk,
        Integer customerCodeFk,
        Integer couponCodeFk,
        String couponIssueBarcode,
        Date couponIssueDate,
        Date couponExpireDate,
        Date couponUseDate,
        String couponName, String customerName, Double couponDiscountRate
	) {
        this.couponIssueCodePk = couponIssueCodePk;
        this.customerCodeFk = customerCodeFk;
        this.couponCodeFk = couponCodeFk;
        this.couponIssueBarcode = couponIssueBarcode;
        this.couponIssueDate = couponIssueDate;
        this.couponExpireDate = couponExpireDate;
        this.couponUseDate = couponUseDate;
		this.couponName = couponName;
		this.customerName = customerName;
		this.couponDiscountRate = couponDiscountRate;
	}
}
