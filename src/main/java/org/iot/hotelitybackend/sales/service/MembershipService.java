package org.iot.hotelitybackend.sales.service;


import org.iot.hotelitybackend.sales.dto.MembershipDTO;

import java.util.List;

public interface MembershipService {

    List<MembershipDTO> selectAllMembership();
}
