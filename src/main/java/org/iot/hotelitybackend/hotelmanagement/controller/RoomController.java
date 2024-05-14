package org.iot.hotelitybackend.hotelmanagement.controller;

import java.util.Map;

import org.hibernate.annotations.Parameter;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelmanagement.service.RoomService;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hotel-management")
public class RoomController {

	private final RoomService roomService;
	private final ModelMapper mapper;

	@Autowired
	public RoomController(RoomService roomService, ModelMapper mapper) {
		this.roomService = roomService;
		this.mapper = mapper;
	}

	@GetMapping("rooms")
	public ResponseEntity<ResponseVO> selectRoomsList(@RequestParam int pageNum) {
		Map<String, Object> roomPageInfo = roomService.selectRoomsList(pageNum);

		ResponseVO response = ResponseVO.builder()
			.data(roomPageInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("rooms/search")
	public ResponseEntity<ResponseVO> selectSearchedRoomsList(
		@RequestParam(required = false) String roomName,
		@RequestParam(required = false) String roomCurrentStatus,
		@RequestParam(required = false) Integer roomSubRoomsCount,
		@RequestParam int pageNum
	) {

		Map<String, Object> roomPageInfo = roomService.selectSearchedRoomsList(pageNum, roomName, roomSubRoomsCount, roomCurrentStatus);

		ResponseVO response = ResponseVO.builder()
			.data(roomPageInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@PutMapping("/rooms/{roomCodePk}")
	public ResponseEntity<ResponseVO> modifyRoomInfo(
		@RequestBody RequestModifyRoom requestModifyRoom,
		@PathVariable("roomCodePk") String roomCodePk) {

		Map<String, Object> modifiedRoomInfo = roomService.modifyRoomInfo(requestModifyRoom, roomCodePk);

		ResponseVO response = ResponseVO.builder()
			.data(modifiedRoomInfo)
			.resultCode(HttpStatus.CREATED.value())
			.message("수정 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
