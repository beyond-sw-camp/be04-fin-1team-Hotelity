package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.Map;

public interface RoomService {
	Map<String, Object> selectRoomsList(int pageNum);
}
