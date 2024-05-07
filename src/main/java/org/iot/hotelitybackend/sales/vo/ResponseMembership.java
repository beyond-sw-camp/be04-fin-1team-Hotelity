package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

@Data
public class ResponseMembership {
    private int membershipLevelCodePk;
    private String membershipLevelName;
    private String membershipInfo;
    private int membershipCriteriaAmount;
}
