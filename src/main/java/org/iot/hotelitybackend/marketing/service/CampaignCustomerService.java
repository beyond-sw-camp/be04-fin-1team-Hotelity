package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;

public interface CampaignCustomerService {
    CampaignCustomerDTO selectCampaignByCampaignSentCustomerCodePk(int campaignSentCustomerCodePk);
}
