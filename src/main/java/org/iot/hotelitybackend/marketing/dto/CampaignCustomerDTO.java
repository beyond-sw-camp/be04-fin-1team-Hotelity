package org.iot.hotelitybackend.marketing.dto;

import lombok.Data;

@Data
public class CampaignCustomerDTO {
    private Integer campaignSentCustomerCodePk;
    private Integer campaignCodeFk;
    private Integer customerCodeFk;
    private Integer reservationCodeFk;
}
