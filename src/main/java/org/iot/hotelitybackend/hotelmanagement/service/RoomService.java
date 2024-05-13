package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.Map;

public interface RoomService {
	Map<String, Object> selectRoomsList(int pageNum);
	Map<String, Object> selectSearchedRoomsList(int pageNum, Integer roomCategoryCodeFk, String roomCurrentStatus);
}
