package org.iot.hotelitybackend.hotelmanagement.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;
import org.iot.hotelitybackend.hotelmanagement.vo.RoomSearchCriteria;

public interface RoomService {

	Map<String, Object> selectSearchedRoomsList(RoomSearchCriteria criteria);

	Map<String, Object> modifyRoomInfo(RequestModifyRoom requestModifyRoom, String roomCodePk);

	Map<String, Object> deleteRoom(String roomCodePk);

	Map<String, Object> selectRoomInfo(String roomCodePk);
}
