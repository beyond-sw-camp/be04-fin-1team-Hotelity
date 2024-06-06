package org.iot.hotelitybackend.sales.service;


import org.iot.hotelitybackend.sales.dto.MembershipDTO;

import java.util.List;
import java.util.Map;

public interface MembershipService {

    Map<String, Object> selectAllMembership(Integer pageNum);
}
