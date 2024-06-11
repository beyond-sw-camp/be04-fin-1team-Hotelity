package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.marketing.aggregate.TemplateEntity;
import org.iot.hotelitybackend.marketing.dto.TemplateDTO;
import org.iot.hotelitybackend.marketing.repository.TemplateRepository;
import org.iot.hotelitybackend.marketing.vo.RequestTemplate;
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
public class TemplateServiceImpl implements TemplateService {

    private final ModelMapper mapper;
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(ModelMapper mapper, TemplateRepository templateRepository) {
        this.mapper = mapper;
        this.templateRepository = templateRepository;
    }

    @Override
    public Map<String, Object> selectTemplatesList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<TemplateEntity> templatePage = templateRepository.findAll(pageable);
        List<TemplateDTO> templateDTOList = templatePage.stream().map(templateEntity -> mapper.map(templateEntity, TemplateDTO.class))
                .toList();

        int totalPagesCount = templatePage.getTotalPages();
        int currentPageIndex = templatePage.getNumber();

        Map<String, Object> templatePageInfo = new HashMap<>();

        templatePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        templatePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        templatePageInfo.put(KEY_CONTENT, templateDTOList);

        return templatePageInfo;
    }

    @Override
    public TemplateDTO selectTemplateByTemplateCodePk(int templateCodePk) {
        TemplateEntity templateEntity = templateRepository.findById(templateCodePk)
                .orElseThrow(IllegalArgumentException::new);

        TemplateDTO templateDTO = mapper.map(templateEntity, TemplateDTO.class);

        return templateDTO;
    }

    @Override
    public Map<String, Object> registTemplate(RequestTemplate requestTemplate) {
        TemplateEntity templateEntity = TemplateEntity.builder()
                .templateName(requestTemplate.getTemplateName())
                .templateContent(requestTemplate.getTemplateContent())
                .build();

        Map<String, Object> registTemplateInfo = new HashMap<>();

        registTemplateInfo.put(KEY_CONTENT, mapper.map(templateRepository.save(templateEntity), TemplateDTO.class));

        return registTemplateInfo;
    }

    @Override
    public Map<String, Object> modifyTemplate(RequestTemplate requestTemplate, int templateCodePk) {
        TemplateEntity templateEntity = TemplateEntity.builder()
                .templateCodePk(templateCodePk)
                .templateName(requestTemplate.getTemplateName())
                .templateContent(requestTemplate.getTemplateContent())
                .build();

        Map<String, Object> modifyTemplateInfo = new HashMap<>();

        modifyTemplateInfo.put(KEY_CONTENT, mapper.map(templateRepository.save(templateEntity), TemplateDTO.class));

        return modifyTemplateInfo;
    }

    @Override
    public Map<String, Object> deleteTemplate(int templateCodePk) {
        Map<String, Object> deleteTemplateInfo = new HashMap<>();

        if (templateRepository.existsById(templateCodePk)) {
            templateRepository.deleteById(templateCodePk);
        } else {
            System.out.println("해당하는 템플릿을 찾을 수 없습니다.");
        }

        return deleteTemplateInfo;
    }

    @Override
    public Map<String, Object> selectAllTemplates() {
        List<TemplateEntity> templateEntityList = templateRepository.findAll();
        List<TemplateDTO> templateDTOList = templateEntityList
            .stream()
            .map(templateEntity -> mapper.map(templateEntity, TemplateDTO.class))
            .toList();

        Map<String, Object> templateListInfo = new HashMap<>();
        templateListInfo.put(KEY_CONTENT, templateDTOList);

        return templateListInfo;
    }
}
