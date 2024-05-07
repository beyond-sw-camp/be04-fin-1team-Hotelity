package org.iot.hotelitybackend.marketing.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
