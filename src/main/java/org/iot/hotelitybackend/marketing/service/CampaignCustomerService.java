package org.iot.hotelitybackend.marketing.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;
import org.iot.hotelitybackend.marketing.vo.CampaignCustomerSearchCriteria;

public interface CampaignCustomerService {
    CampaignCustomerDTO selectCampaignByCampaignSentCustomerCodePk(int campaignSentCustomerCodePk);

    Map<String, Object> selectSearchedCampaignsList(CampaignCustomerSearchCriteria criteria);
}
