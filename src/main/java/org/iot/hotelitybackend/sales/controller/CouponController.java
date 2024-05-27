package org.iot.hotelitybackend.sales.controller;

import static org.iot.hotelitybackend.common.constant.Constant.*;
import static org.iot.hotelitybackend.common.util.ExcelUtil.*;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.aggregate.CouponEntity;
import org.iot.hotelitybackend.sales.dto.CouponDTO;
import org.iot.hotelitybackend.sales.service.CouponService;
import org.iot.hotelitybackend.sales.vo.RequestCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sales")
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons/page")
    public ResponseEntity<ResponseVO> selectAllCouponsType(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) Integer couponCodePk,
        @RequestParam(required = false) String couponName,
        @RequestParam(required = false) String couponType,
        @RequestParam(required = false) Double couponDiscountRate,
        @RequestParam(required = false) Date couponLaunchingDate,
        @RequestParam(required = false) String couponInfo,
        @RequestParam(required = false) Integer membershipLevelCodeFk,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) Integer sortBy
    ) {
        Map<String, Object> couponPageInfo = couponService.selectAllCouponsType(pageNum, couponCodePk
            , couponName, couponType, couponDiscountRate, couponLaunchingDate, couponInfo, membershipLevelCodeFk,
            orderBy, sortBy);

        ResponseVO response = ResponseVO.builder()
            .data(couponPageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PostMapping("/coupons")
    public ResponseEntity<ResponseVO> registCoupon(@RequestBody RequestCoupon requestCoupon) {
        Map<String, Object> registCouponInfo = couponService.registCoupon(requestCoupon);

        ResponseVO response = ResponseVO.builder()
            .data(registCouponInfo)
            .resultCode(HttpStatus.CREATED.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PutMapping("/{couponCodePk}/coupons")
    public ResponseEntity<ResponseVO> modifyCoupon(
        @RequestBody RequestCoupon requestCoupon,
        @PathVariable("couponCodePk") int couponCodePk
    ) {
        Map<String, Object> modifyCouponInfo = couponService.modifyCoupon(requestCoupon, couponCodePk);

        ResponseVO response = ResponseVO.builder()
            .data(modifyCouponInfo)
            .resultCode(HttpStatus.CREATED.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/coupons/page/excel/download")
    public ResponseEntity<InputStreamResource> downloadAllCouponsType(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) Integer couponCodePk,
        @RequestParam(required = false) String couponName,
        @RequestParam(required = false) String couponType,
        @RequestParam(required = false) Double couponDiscountRate,
        @RequestParam(required = false) Date couponLaunchingDate,
        @RequestParam(required = false) String couponInfo,
        @RequestParam(required = false) Integer membershipLevelCodeFk,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) Integer sortBy
    ) {
        // 파일명을 적어주세요.
        String title = "쿠폰종류";

        // 컬럼명은 DTO 에 적혀있는 필드 순서대로 적어주셔야 합니다,,,
        String[] headerStrings = {
            "쿠폰코드", "쿠폰이름", "쿠폰타입", "쿠폰할인율",
            "쿠폰개시일", "쿠폰상세정보", "멤버십등급코드", "멤버십등급명"
        };

        // 조회해서 DTO 리스트 가져오기
        Map<String, Object> couponPageInfo = couponService.selectAllCouponsType(pageNum, couponCodePk
            , couponName, couponType, couponDiscountRate, couponLaunchingDate, couponInfo, membershipLevelCodeFk,
            orderBy, sortBy);

        try {
            // 엑셀 시트와 파일 만들기
            Map<String, Object> result = createExcelFile((List<CouponDTO>)couponPageInfo.get(KEY_CONTENT), title,
                headerStrings);

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
