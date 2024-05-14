package org.iot.hotelitybackend.marketing.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.marketing.dto.TemplateDTO;
import org.iot.hotelitybackend.marketing.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/marketing")
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/templates/page")
    public ResponseEntity<ResponseVO> selectTemplatesList(@RequestParam int pageNum) {
        Map<String, Object> templatePageInfo = templateService.selectTemplatesList(pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(templatePageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/templates/{templateCodePk}/template")
    public TemplateDTO selectTemplateByTemplateCodePk(@PathVariable int templateCodePk) {
        return templateService.selectTemplateByTemplateCodePk(templateCodePk);
    }
}
