package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MembershipIssueDTO {
    private Integer membershipIssueCodePk;
    private Integer customerCodeFk;
    private Date membershipIssueDate;
    private Integer membershipLevelCodeFk;
    private String membershipLevelName;
    private String customerName;
}
