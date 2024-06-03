package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.service.NoticeService;
import org.iot.hotelitybackend.sales.vo.RequestModifyNotice;
import org.iot.hotelitybackend.sales.vo.RequestNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<ResponseVO> selectNoticesList(
        @RequestParam int pageNum,
        @RequestParam(required = false) Integer noticeCodePk,
        @RequestParam(required = false) String noticeTitle,
        @RequestParam(required = false) String noticeContent,
        @RequestParam(required = false) Integer employeeCodeFk,
        @RequestParam(required = false) String employeeName,
        @RequestParam(required = false) String branchCodeFk,
        @RequestParam(required = false) LocalDateTime noticePostedDate,
        @RequestParam(required = false) LocalDateTime noticeLastUpdatedDate,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) Integer sortBy
    ) {
        Map<String, Object> noticePageInfo = noticeService.selectNoticesList(
            pageNum, noticeCodePk, noticeTitle, noticeContent, employeeCodeFk,
            employeeName, branchCodeFk, noticePostedDate, noticeLastUpdatedDate,
            orderBy, sortBy);

        ResponseVO response = ResponseVO.builder()
            .data(noticePageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    /* 최신순 공지 3개 조회 (대시보드용) */
    @GetMapping("/notices/latest")
    public ResponseEntity<ResponseVO> selectLatestNoticeList() {
        Map<String, Object> latestNoticeList = noticeService.selectLatestNoticeList();

        ResponseVO response = ResponseVO.builder()
            .data(latestNoticeList)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/notices/{noticeCodePk}/notice")
    public NoticeDTO selectNoticeByNoticeCodePk(@PathVariable int noticeCodePk) {
        return noticeService.selectNoticeByNoticeCodePk(noticeCodePk);
    }

    @PostMapping("/notices")
    public ResponseEntity<ResponseVO> registNotice(@RequestBody RequestNotice requestNotice) {
        Map<String, Object> registNoticeInfo = noticeService.registNotice(requestNotice);

        ResponseVO response = ResponseVO.builder()
                .data(registNoticeInfo)
                .resultCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PutMapping("/notices/{noticeCodePk}")
    public ResponseEntity<ResponseVO> modifyNotice(
            @RequestBody RequestModifyNotice requestModifyNotice,
            @PathVariable ("noticeCodePk") int noticeCodePk
    ) {
        Map<String, Object> modifyNoticeInfo = noticeService.modifyNotice(requestModifyNotice, noticeCodePk);

        ResponseVO response = ResponseVO.builder()
                .data(modifyNoticeInfo)
                .resultCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @DeleteMapping("/notices/{noticeCodePk}")
    public ResponseEntity<ResponseVO> deleteNotice(@PathVariable("noticeCodePk") int noticeCodePk) {

        Map<String, Object> deleteNotice = noticeService.deleteNotice(noticeCodePk);

        ResponseVO response = ResponseVO.builder()
                .data(deleteNotice)
                .resultCode(HttpStatus.NO_CONTENT.value())
                .message("삭제 성공")
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);

    }
}
