package org.iot.hotelitybackend.hotelmanagement.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;

public interface RoomService {

	Map<String, Object> selectSearchedRoomsList(
		Integer pageNum,
		String roomCodePk,
		String branchCodeFk,
		Integer roomNumber,
		String roomName,
		String roomCurrentStatus,
		Float roomDiscountRate,
		String roomView,
		Integer roomSubRoomsCount
	);

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
