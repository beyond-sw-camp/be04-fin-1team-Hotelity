package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.service.CouponIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
