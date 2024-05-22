package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CouponIssueDTO {
    private Integer couponIssueCodePk;
    private Integer customerCodeFk;
    private Integer couponCodeFk;
    private String couponIssueBarcode;
    private Date couponIssueDate;
    private Date couponExpireDate;
    private Date couponUseDate;
    private String membershipLevelName;
    private String customerName;
}
