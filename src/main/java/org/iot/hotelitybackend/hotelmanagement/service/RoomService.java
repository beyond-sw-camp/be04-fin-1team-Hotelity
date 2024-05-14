package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;

public interface RoomService {
	Map<String, Object> selectRoomsList(int pageNum);
	Map<String, Object> selectSearchedRoomsList(int pageNum, String roomName, Integer roomSubRoomsCount, String roomCurrentStatus);
	Map<String, Object> modifyRoomInfo(RequestModifyRoom requestModifyRoom, String roomCodePk);
}
