package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.marketing.dto.TemplateDTO;
import org.iot.hotelitybackend.marketing.vo.RequestTemplate;

import java.util.Map;

public interface TemplateService {
    Map<String, Object> selectTemplatesList(int pageNum);

    TemplateDTO selectTemplateByTemplateCodePk(int templateCodePk);

    Map<String, Object> registTemplate(RequestTemplate requestTemplate);

    Map<String, Object> modifyTemplate(RequestTemplate requestTemplate, int templateCodePk);

    Map<String, Object> deleteTemplate(int templateCodePk);
}
