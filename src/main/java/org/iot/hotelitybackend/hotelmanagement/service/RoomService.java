package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;

public interface RoomService {
	Map<String, Object> selectRoomsList(int pageNum);
	Map<String, Object> selectSearchedRoomsList(int pageNum, Integer roomCategoryCodeFk, String roomCurrentStatus);
	Map<String, Object> modifyRoomInfo(RequestModifyRoom requestModifyRoom, String roomCodePk);
}
