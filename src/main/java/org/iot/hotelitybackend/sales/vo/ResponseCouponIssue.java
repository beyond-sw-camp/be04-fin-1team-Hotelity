package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseCouponIssue {
    private int couponIssueCodePk;
    private int customerCodeFk;
    private int couponCodeFk;
    private String couponIssueBarcode;
    private Date couponIssueDate;
    private Date couponExpireDate;
    private Date couponUseDate;
}
