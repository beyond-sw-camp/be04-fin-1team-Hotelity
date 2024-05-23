package org.iot.hotelitybackend.hotelmanagement.service;

import org.iot.hotelitybackend.hotelmanagement.dto.AncillaryDTO;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyFacility;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistFacility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AncillaryService {
	Map<String, Object> selectAllFacilities(int pageNum);

    Map<String, Object> registFacility(RequestRegistFacility requestRegistFacility);

    Map<String, Object> modifyFacilityInfo(RequestModifyFacility requestModifyFacility, int ancillaryCodePk);

    Map<String, Object> deleteFacilityInfo(int ancillaryCodePk);

	List<AncillaryDTO> selectAllFacilitiesForExcel();

	Map<String, Object> createFacilitiesExcelFile(List<AncillaryDTO> ancillaryDTOList) throws IOException,
		NoSuchFieldException, IllegalAccessException;
}
