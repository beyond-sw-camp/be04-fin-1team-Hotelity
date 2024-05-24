package org.iot.hotelitybackend.marketing.service;

import java.util.Date;
import java.util.Map;

public interface CampaignService {
    Map<String, Object> selectCampaignsList(int pageNum);

    Map<String, Object> selectSearchedCampaignsList(int pageNum, String campaignSendType, Integer employeeCodeFk, Date campaignSentDate, Integer campaignCustomerPk, String customerName, String membershipName, String campaignName, Integer campaignSentStatus,
        Integer templatePk, Integer templateName);
}
