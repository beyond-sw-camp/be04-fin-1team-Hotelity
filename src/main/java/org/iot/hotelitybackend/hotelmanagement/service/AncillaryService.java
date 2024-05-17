package org.iot.hotelitybackend.hotelmanagement.service;

import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyFacility;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistFacility;

import java.util.Map;

public interface AncillaryService {
	Map<String, Object> selectAllFacilities(int pageNum);

    Map<String, Object> registFacility(RequestRegistFacility requestRegistFacility);

    Map<String, Object> modifyFacilityInfo(RequestModifyFacility requestModifyFacility, int ancillaryCodePk);

    Map<String, Object> deleteFacilityInfo(int ancillaryCodePk);
}
