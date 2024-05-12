package org.iot.hotelitybackend.sales.controller;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.iot.hotelitybackend.sales.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
public class MembershipController {

    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/membership")
    public ResponseEntity<MembershipDTO> selectAllMembership() {
        return null;
    }

    @GetMapping("/membership/{MembershipLevelCodePk}")
    public CustomerDTO selectCustomerByMembershipLevelCodePk(@PathVariable int membershipLevelCodePk) {
        return membershipService.selectCustomerByMembershipLevelCodePk(membershipLevelCodePk);
    }
}
