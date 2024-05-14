package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.marketing.dto.TemplateDTO;

import java.util.Map;

public interface TemplateService {
    Map<String, Object> selectTemplatesList(int pageNum);

    TemplateDTO selectTemplateByTemplateCodePk(int templateCodePk);
}
