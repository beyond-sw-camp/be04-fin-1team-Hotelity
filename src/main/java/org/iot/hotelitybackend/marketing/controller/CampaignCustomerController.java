package org.iot.hotelitybackend.marketing.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;
import org.iot.hotelitybackend.marketing.service.CampaignCustomerService;
import org.iot.hotelitybackend.marketing.vo.CampaignCustomerSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    @GetMapping("/campaigns/search/page")
    public ResponseEntity<ResponseVO> selectSearchedCampaignCustomersList(CampaignCustomerSearchCriteria criteria) {
        Map<String, Object> campaignPageInfo = campaignCustomerService.selectSearchedCampaignsList(criteria);

        ResponseVO response = ResponseVO.builder()
            .data(campaignPageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
