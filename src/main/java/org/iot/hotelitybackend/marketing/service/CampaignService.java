package org.iot.hotelitybackend.marketing.service;

import java.util.Map;

public interface CampaignService {
    Map<String, Object> selectCampaignsList(int pageNum);
}
