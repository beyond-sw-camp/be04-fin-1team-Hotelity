package org.iot.hotelitybackend.marketing.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.marketing.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/marketing")
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/campaigns/page")
    public ResponseEntity<ResponseVO> selectCampaignsList(@RequestParam int pageNum) {
        Map<String, Object> campaignPageInfo = campaignService.selectCampaignsList(pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(campaignPageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }


}
