package org.iot.hotelitybackend.marketing.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseCampaign {
    private Integer campaignCodePk;
    private String campaignSendType;
    private String campaignContent;
    private Date campaignSentDate;
    private Integer campaignSentStatus;
    private Integer templateCodeFk;
    private String employeeCodeFk;
    private String campaignTitle;
}
