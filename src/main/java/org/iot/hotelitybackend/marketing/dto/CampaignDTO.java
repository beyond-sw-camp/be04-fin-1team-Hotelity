package org.iot.hotelitybackend.marketing.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CampaignDTO {
    private Integer campaignCodePk;
    private String campaignSendType;
    private String campaignContent;
    private Date campaignSentDate;
    private Integer campaignSentStatus;
    private Integer templateCodeFk;
    private String employeeCodeFk;
    private String campaignTitle;
}
