package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MembershipIssueDTO {
    private int membershipIssueCodePk;
    private int customerCodeFk;
    private Date membershipIssueDate;
    private int membershipLevelCodeFk;
}
