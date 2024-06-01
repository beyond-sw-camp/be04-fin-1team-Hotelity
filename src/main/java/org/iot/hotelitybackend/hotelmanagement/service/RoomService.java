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
		Integer roomSubRoomsCount,
		Integer minPrice,
		Integer maxPrice,
		Integer roomPrice,
		Integer roomCapacity,
		Integer roomBathroomCount,
		String roomSpecificInfo,
		String orderBy,
		Integer sortBy
	);

	Map<String, Object> modifyRoomInfo(RequestModifyRoom requestModifyRoom, String roomCodePk);

	Map<String, Object> deleteRoom(String roomCodePk);

}
