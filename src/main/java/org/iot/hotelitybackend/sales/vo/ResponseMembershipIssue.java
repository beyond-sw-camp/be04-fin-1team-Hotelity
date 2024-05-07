package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseMembershipIssue {
    private int membershipIssueCodePk;
    private int customerCodeFk;
    private Date membershipIssueDate;
    private int membershipLevelCodeFk;
}
