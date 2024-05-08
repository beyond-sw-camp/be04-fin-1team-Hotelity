package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseCouponIssue {
    private Integer couponIssueCodePk;
    private Integer customerCodeFk;
    private Integer couponCodeFk;
    private String couponIssueBarcode;
    private Date couponIssueDate;
    private Date couponExpireDate;
    private Date couponUseDate;
}
