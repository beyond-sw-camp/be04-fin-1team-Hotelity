package org.iot.hotelitybackend.marketing.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;

public interface CampaignCustomerService {
    CampaignCustomerDTO selectCampaignByCampaignSentCustomerCodePk(int campaignSentCustomerCodePk);

    Map<String, Object> selectSearchedCampaignsList(int pageNum, Integer campaignCodeFk, String campaignSendType,
        LocalDateTime campaignSentDate, String customerName,
        String campaignTitle, Integer campaignSentStatus, Integer templateCodeFk, String templateName);
}
