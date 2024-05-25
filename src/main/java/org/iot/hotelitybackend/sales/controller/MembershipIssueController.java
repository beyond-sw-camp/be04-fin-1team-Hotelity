package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.sales.dto.MembershipIssueDTO;
import org.iot.hotelitybackend.sales.service.MembershipIssueService;
import org.iot.hotelitybackend.sales.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
public class MembershipIssueController {

    private final MembershipIssueService membershipIssueService;


    @Autowired
    public MembershipIssueController(MembershipIssueService membershipIssueService) {
        this.membershipIssueService = membershipIssueService;
    }

    // @GetMapping("/assign")
    // @Scheduled(cron = "0 0 9 1 3 ?") // 매년 3월 1일 09:00:00에 실행
    public String assignMembershipBasedOnPayments() {
        membershipIssueService.assignMembershipBasedOnPayments();
        return null;
    }

}
