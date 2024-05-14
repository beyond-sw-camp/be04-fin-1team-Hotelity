package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.Map;

public interface AncillaryService {
	Map<String, Object> selectAllFacilities(int pageNum);
}
