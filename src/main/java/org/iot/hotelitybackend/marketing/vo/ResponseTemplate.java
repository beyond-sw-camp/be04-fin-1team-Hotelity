package org.iot.hotelitybackend.marketing.vo;

import lombok.Data;

@Data
public class ResponseTemplate {
    private Integer templateCodePk;
    private String templateName;
    private String templateContent;
}
