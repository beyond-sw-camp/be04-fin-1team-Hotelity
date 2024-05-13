package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sales")
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons/page")
    public ResponseEntity<ResponseVO> selectAllCouponsType(@RequestParam int pageNum) {
        Map<String, Object> couponPageInfo = couponService.selectAllCouponsType(pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(couponPageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/coupons/{couponCodePk}/coupon")
    public CouponDTO selectCouponByCouponCodePk(@PathVariable int couponCodePk) {
        return couponService.selectCouponByCouponCodePk(couponCodePk);
    }
}
