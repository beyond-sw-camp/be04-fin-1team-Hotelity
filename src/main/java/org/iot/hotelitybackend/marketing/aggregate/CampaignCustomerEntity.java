package org.iot.hotelitybackend.marketing.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campaign_customer_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CampaignCustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer campaignSentCustomerCodePk;
    private Integer campaignCodeFk;
    private Integer customerCodeFk;
    private Integer reservationCodeFk;
}
