package org.iot.hotelitybackend.customer.service;

import java.util.List;

import org.iot.hotelitybackend.customer.dto.NationDTO;
import org.iot.hotelitybackend.customer.vo.ResponseNation;

public interface NationService {
	List<NationDTO> selectNationList();
}
