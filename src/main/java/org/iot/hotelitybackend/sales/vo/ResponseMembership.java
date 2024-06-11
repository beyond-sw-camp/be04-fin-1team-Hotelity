package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

@Data
public class ResponseMembership {
    private Integer membershipLevelCodePk;
    private String membershipLevelName;
    private String membershipInfo;
    private Integer membershipCriteriaAmount;
}
