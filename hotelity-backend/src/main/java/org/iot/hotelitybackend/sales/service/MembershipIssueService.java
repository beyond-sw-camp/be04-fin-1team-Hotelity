package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.MembershipIssueDTO;

public interface MembershipIssueService {
    void assignMembershipBasedOnPayments();

//    MembershipIssueDTO selectAllMembershipIssueList(int membershipIssueCodePk, int pageNum);
}
