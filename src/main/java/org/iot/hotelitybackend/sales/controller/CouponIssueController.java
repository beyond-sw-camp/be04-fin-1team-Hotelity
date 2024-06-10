package org.iot.hotelitybackend.sales.controller;

import lombok.extern.slf4j.Slf4j;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.CouponIssueDTO;
import org.iot.hotelitybackend.sales.service.CouponIssueService;
import org.iot.hotelitybackend.sales.vo.CouponIssueSearchCriteria;
import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;
import static org.iot.hotelitybackend.common.util.ExcelType.COUPON_ISSUE;
import static org.iot.hotelitybackend.common.util.ExcelUtil.createExcelFile;

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
        @ModelAttribute CouponIssueSearchCriteria criteria
    ) {
        Map<String, Object> couponIssuePageInfo = couponIssueService.selectCouponIssueList(
            // pageNum, couponIssueCodePk, couponName, customerCodePk, customerName, couponDiscountRate, couponIssueDate,
            // couponExpireDate, couponUseDate, orderBy, sortBy
            criteria
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
        @ModelAttribute CouponIssueSearchCriteria criteria
    ) {

        // 조회해서 DTO 리스트 가져오기
        Map<String, Object> couponIssueListInfo = couponIssueService.selectCouponIssueList(
            // pageNum, couponIssueCodePk, couponName, customerCodePk, customerName, couponDiscountRate, couponIssueDate,
            // couponExpireDate, couponUseDate, orderBy, sortBy
            criteria
        );

        try {

            Map<String, Object> result = createExcelFile(
                    (List<CouponIssueDTO>)couponIssueListInfo.get(KEY_CONTENT),
                    COUPON_ISSUE.getFileName(),
                    COUPON_ISSUE.getHeaderStrings()
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
