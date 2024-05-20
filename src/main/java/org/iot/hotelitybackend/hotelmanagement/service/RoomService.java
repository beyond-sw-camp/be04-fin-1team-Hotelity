package org.iot.hotelitybackend.hotelmanagement.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;

public interface RoomService {
	Map<String, Object> selectRoomsList(int pageNum);

	Map<String, Object> selectSearchedRoomsList(int pageNum, String roomName, Integer roomSubRoomsCount, String roomCurrentStatus, String branchCodeFk);

	Map<String, Object> modifyRoomInfo(RequestModifyRoom requestModifyRoom, String roomCodePk);

	Map<String, Object> deleteRoom(String roomCodePk);

	List<RoomDTO> selectRoomsForExcel();

	Map<String, Object> selectSearchedRoomsForExcel(String roomName, Integer roomSubRoomsCount, String roomCurrentStatus, String branchCodeFk);

	// List<RoomDTO> pageToList(Map<String, Object> roomPageInfo);

	// List<RoomDTO> pageToSearchedList(Map<String, Object> stringObjectMap, String roomName, Integer roomSubRoomsCount, String roomCurrentStatus, String branchCodeFk);

	Map<String, Object> createRoomsExcelFile(List<RoomDTO> roomDTOList) throws
		IOException,
		NoSuchFieldException,
		IllegalAccessException;
}
