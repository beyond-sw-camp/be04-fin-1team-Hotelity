package org.iot.hotelitybackend.marketing.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class CampaignCustomerDTO {
    private Integer campaignSentCustomerCodePk;
    private Integer campaignCodeFk;
    private Integer customerCodeFk;
    private Integer reservationCodeFk;
    private String customerName;
    private String campaignSendType;
    private String campaignContent;
    private LocalDateTime campaignSentDate;
    private Integer campaignSentStatus;
    private Integer templateCodeFk;
    private String templateName;
    private String employeeName;
    private String campaignTitle;
    private String membershipLevelName;
}
