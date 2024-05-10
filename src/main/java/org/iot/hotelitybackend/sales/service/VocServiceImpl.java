package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.aggregate.VocEntity;
import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.repository.VocRepository;
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
public class VocServiceImpl implements VocService {

    private final ModelMapper mapper;

    private final VocRepository vocRepository;

    @Autowired
    public VocServiceImpl(ModelMapper mapper, VocRepository vocRepository) {
        this.mapper = mapper;
        this.vocRepository = vocRepository;
    }

    @Override
    public Map<String, Object> selectVocsList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<VocEntity> vocPage = vocRepository.findAll(pageable);
        List<VocDTO> vocDTOList = vocPage.stream().map(vocEntity -> mapper.map(vocEntity, VocDTO.class)).toList();

        int totalPagesCount = vocPage.getTotalPages();
        int currentPageIndex = vocPage.getNumber();

        Map<String, Object> vocPageInfo = new HashMap<>();

        vocPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        vocPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        vocPageInfo.put(KEY_CONTENT, vocDTOList);

        return vocPageInfo;
    }
}
