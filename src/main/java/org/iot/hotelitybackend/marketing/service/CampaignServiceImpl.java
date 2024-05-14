package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.iot.hotelitybackend.marketing.dto.CampaignDTO;
import org.iot.hotelitybackend.marketing.repository.CampaignRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final ModelMapper mapper;
    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignServiceImpl(ModelMapper mapper, CampaignRepository campaignRepository) {
        this.mapper = mapper;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Map<String, Object> selectCampaignsList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<CampaignEntity> campaignPage = campaignRepository.findAll(pageable);
        List<CampaignDTO> campaignDTOList = campaignPage.stream().map(campaignEntity -> mapper.map(campaignEntity, CampaignDTO.class))
                .toList();

        int totalPagesCount = campaignPage.getTotalPages();
        int currentPageIndex = campaignPage.getNumber();

        Map<String, Object> campaignPageInfo = new HashMap<>();

        campaignPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        campaignPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        campaignPageInfo.put(KEY_CONTENT, campaignDTOList);

        return campaignPageInfo;
    }
}
