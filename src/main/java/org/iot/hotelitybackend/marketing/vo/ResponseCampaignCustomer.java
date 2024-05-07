package org.iot.hotelitybackend.marketing.vo;

import lombok.Data;

@Data
public class ResponseCampaignCustomer {
    private Integer campaignSentCustomerCodePk;
    private Integer campaignCodeFk;
    private Integer customerCodeFk;
    private Integer reservationCodeFk;
}
