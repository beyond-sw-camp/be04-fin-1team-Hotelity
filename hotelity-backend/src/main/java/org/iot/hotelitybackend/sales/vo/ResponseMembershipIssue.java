package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseMembershipIssue {
    private Integer membershipIssueCodePk;
    private Integer customerCodeFk;
    private Date membershipIssueDate;
    private Integer membershipLevelCodeFk;
}
