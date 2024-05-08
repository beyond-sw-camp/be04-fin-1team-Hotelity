package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

@Data
public class MembershipDTO {
    private Integer membershipLevelCodePk;
    private String membershipLevelName;
    private String membershipInfo;
    private Integer membershipCriteriaAmount;
}
