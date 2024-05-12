package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sales")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notices/page")
    public ResponseEntity<ResponseVO> selectNoticesList(@RequestParam int pageNum) {
        Map<String, Object> noticePageInfo = noticeService.selectNoticesList(pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(noticePageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/notices/{noticeCodePk}/notice")
    public NoticeDTO selectNoticeByNoticeCodePk(@PathVariable int noticeCodePk) {
        return noticeService.selectNoticeByNoticeCodePk(noticeCodePk);
    }
}
