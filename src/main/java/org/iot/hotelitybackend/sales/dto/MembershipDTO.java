package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

@Data
public class MembershipDTO {
    private int membershipLevelCodePk;
    private String membershipLevelName;
    private String membershipInfo;
    private int membershipCriteriaAmount;
}
