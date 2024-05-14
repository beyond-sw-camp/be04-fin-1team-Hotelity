package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.service.CouponIssueService;
import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sales")
public class CouponIssueController {

    private final CouponIssueService couponIssueService;

    @Autowired
    public CouponIssueController(CouponIssueService couponIssueService) {
        this.couponIssueService = couponIssueService;
    }

    @GetMapping("/coupons/issue/page")
    public ResponseEntity<ResponseVO> selectCouponIssueList(@RequestParam int pageNum) {
        Map<String, Object> couponIssuePageInfo = couponIssueService.selectCouponIssueList(pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(couponIssuePageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PostMapping("coupons/issue")
    public ResponseEntity<ResponseVO> registCouponToCustomer(@RequestBody RequestCouponIssue requestCouponIssue) {
        Map<String, Object> registCouponIssueInfo = couponIssueService.registCouponToCustomer(requestCouponIssue);

        ResponseVO response = ResponseVO.builder()
                .data(registCouponIssueInfo)
                .resultCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
