package org.iot.hotelitybackend.marketing.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;
import org.iot.hotelitybackend.marketing.service.CampaignCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/marketing")
public class CampaignCustomerController {

    private final CampaignCustomerService campaignCustomerService;

    @Autowired
    public CampaignCustomerController(CampaignCustomerService campaignCustomerService) {
        this.campaignCustomerService = campaignCustomerService;
    }

    @GetMapping("/campaigns/{campaignSentCustomerCodePk}/campaign")
    public CampaignCustomerDTO selectCampaignByCampaignSentCustomerCodePk(@PathVariable int campaignSentCustomerCodePk) {
        return campaignCustomerService.selectCampaignByCampaignSentCustomerCodePk(campaignSentCustomerCodePk);
    }
}
