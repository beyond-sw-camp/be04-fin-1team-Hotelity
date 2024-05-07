package org.iot.hotelitybackend.marketing.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "campaign_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CampaignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer campaignCodePk;
    private String campaignSendType;
    private String campaignContent;
    private Date campaignSentDate;
    private Integer campaignSentStatus;
    private Integer templateCodeFk;
    private String employeeCodeFk;
    private String campaignTitle;

    @Builder
    public CampaignEntity(
            Integer campaignCodePk,
            String campaignSendType,
            String campaignContent,
            Date campaignSentDate,
            Integer campaignSentStatus,
            Integer templateCodeFk,
            String employeeCodeFk,
            String campaignTitle
    ) {
        this.campaignCodePk = campaignCodePk;
        this.campaignSendType = campaignSendType;
        this.campaignContent = campaignContent;
        this.campaignSentDate = campaignSentDate;
        this.campaignSentStatus = campaignSentStatus;
        this.templateCodeFk = templateCodeFk;
        this.employeeCodeFk = employeeCodeFk;
        this.campaignTitle = campaignTitle;
    }
}
