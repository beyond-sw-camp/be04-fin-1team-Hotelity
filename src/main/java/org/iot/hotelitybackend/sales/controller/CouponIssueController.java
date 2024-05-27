package org.iot.hotelitybackend.sales.controller;

import static org.iot.hotelitybackend.common.constant.Constant.*;
import static org.iot.hotelitybackend.common.util.ExcelUtil.*;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.CouponIssueDTO;
import org.iot.hotelitybackend.sales.service.CouponIssueService;
import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sales")
public class CouponIssueController {

    private final CouponIssueService couponIssueService;

    @Autowired
    public CouponIssueController(CouponIssueService couponIssueService) {
        this.couponIssueService = couponIssueService;
    }

    @GetMapping("/coupons/issue/page")
    public ResponseEntity<ResponseVO> selectCouponIssueList(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) Integer couponIssueCodePk,
        @RequestParam(required = false) String couponName,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) Integer customerCodePk,
        @RequestParam(required = false) Double couponDiscountRate,
        @RequestParam(required = false) LocalDateTime couponIssueDate,
        @RequestParam(required = false) LocalDateTime couponExpireDate,
        @RequestParam(required = false) LocalDateTime couponUseDate,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) Integer sortBy
    ) {
        Map<String, Object> couponIssuePageInfo = couponIssueService.selectCouponIssueList(
            pageNum, couponIssueCodePk, couponName, customerCodePk, customerName, couponDiscountRate, couponIssueDate,
            couponExpireDate, couponUseDate, orderBy, sortBy
        );

        ResponseVO response = ResponseVO.builder()
            .data(couponIssuePageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PostMapping("/coupons/issue")
    public ResponseEntity<ResponseVO> registCouponToCustomer(@RequestBody RequestCouponIssue requestCouponIssue) {
        Map<String, Object> registCouponIssueInfo = couponIssueService.registCouponToCustomer(requestCouponIssue);

        ResponseVO response = ResponseVO.builder()
            .data(registCouponIssueInfo)
            .resultCode(HttpStatus.CREATED.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/coupons/issue/page/excel/download")
    public ResponseEntity<InputStreamResource> downloadCouponIssueList(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) Integer couponIssueCodePk,
        @RequestParam(required = false) String couponName,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) Integer customerCodePk,
        @RequestParam(required = false) Double couponDiscountRate,
        @RequestParam(required = false) LocalDateTime couponIssueDate,
        @RequestParam(required = false) LocalDateTime couponExpireDate,
        @RequestParam(required = false) LocalDateTime couponUseDate,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) Integer sortBy
    ) {
        // 파일명을 적어주세요.
        String title = "쿠폰발급";

        // 컬럼명은 DTO 에 적혀있는 필드 순서대로 적어주셔야 합니다,,,
        String[] headerStrings = {
            "쿠폰발급코드", "고객코드", "쿠폰코드", "쿠폰발급바코드",
            "쿠폰발급일자", "쿠폰만료일자", "쿠폰사용일자", "멤버십등급명",
            "고객이름", "쿠폰명", "쿠폰할인율"
        };

        // 조회해서 DTO 리스트 가져오기
        Map<String, Object> couponIssueListInfo = couponIssueService.selectCouponIssueList(
            pageNum, couponIssueCodePk, couponName, customerCodePk, customerName, couponDiscountRate, couponIssueDate,
            couponExpireDate, couponUseDate, orderBy, sortBy
        );

        try {

            Map<String, Object> result = createExcelFile(
                (List<CouponIssueDTO>)couponIssueListInfo.get(KEY_CONTENT),
                title,
                headerStrings
            );

            return ResponseEntity
                .ok()
                .headers((HttpHeaders)result.get("headers"))
                .body(new InputStreamResource((ByteArrayInputStream)result.get("result")));

        } catch (Exception e) {

            log.info(e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
