package org.iot.hotelitybackend.sales.service;


import java.util.Map;

public interface MembershipService {

    Map<String, Object> selectAllMembership(Integer pageNum, Integer membershipLevelCodePk, String membershipLevelName,
        String membershipInfo, Integer membershipCriteriaAmount);
}
