package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.service.VocService;
import org.iot.hotelitybackend.sales.vo.ResponseVoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sales")
public class VocController {

    private final VocService vocService;

    @Autowired
    public VocController(VocService vocService) {
        this.vocService = vocService;
    }

    @GetMapping("/vocs/page")
    public ResponseEntity<ResponseVO> selectVocsList(@RequestParam int pageNum) {
        Map<String, Object> vocPageInfo = vocService.selectVocsList(pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(vocPageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/vocs/{vocCodePk}/voc")
    public VocDTO selectVocByVocCodePk(@PathVariable int vocCodePk) {
        return vocService.selectVocByVocCodePk(vocCodePk);
    }
}
