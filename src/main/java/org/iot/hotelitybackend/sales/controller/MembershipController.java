package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.iot.hotelitybackend.sales.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class MembershipController {

    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/membership")
    public ResponseEntity<ResponseVO> selectAllMembership(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) Integer membershipLevelCodePk,
        @RequestParam(required = false) String membershipLevelName,
        @RequestParam(required = false) String membershipInfo,
        @RequestParam(required = false) Integer membershipCriteriaAmount

    ) {

        Map<String, Object> couponIssuePageInfo = membershipService.selectAllMembership(
            pageNum, membershipLevelCodePk, membershipLevelName, membershipInfo, membershipCriteriaAmount
        );

        ResponseVO response = ResponseVO.builder()
            .data(couponIssuePageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);    }
}
