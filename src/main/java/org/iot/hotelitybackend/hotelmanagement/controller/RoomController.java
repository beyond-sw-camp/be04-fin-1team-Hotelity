package org.iot.hotelitybackend.hotelmanagement.controller;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.service.RoomService;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("hotel-management")
@Slf4j
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
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam int pageNum
	) {

		Map<String, Object> roomPageInfo = roomService.selectSearchedRoomsList(pageNum, roomName, roomSubRoomsCount,
			roomCurrentStatus, branchCodeFk);

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

	@DeleteMapping("/rooms/{roomCodePk}")
	public ResponseEntity<ResponseVO> deleteBranch(@PathVariable("roomCodePk") String roomCodePk) {
		Map<String, Object> deleteRoom = roomService.deleteRoom(roomCodePk);
		ResponseVO response = ResponseVO.builder()
			.data(deleteRoom)
			.resultCode(HttpStatus.NO_CONTENT.value())
			.message("삭제 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("/rooms/excel/download")
	public ResponseEntity<InputStreamResource> downloadAllRoomsAsExcel() {
		try {
			List<RoomDTO> roomDTOList = roomService.pageToList(roomService.selectRoomsList(0));
			Map<String, Object> result = roomService.createRoomsExcelFile(roomDTOList);

			return ResponseEntity
				.ok()
				.headers((HttpHeaders)result.get("headers"))
				.body(new InputStreamResource((ByteArrayInputStream)result.get("result")));

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("rooms/search/excel/download")
	public ResponseEntity<InputStreamResource> downloadSearchedRoomsAsExcel(
		@RequestParam(required = false) String roomName,
		@RequestParam(required = false) String roomCurrentStatus,
		@RequestParam(required = false) Integer roomSubRoomsCount,
		@RequestParam(required = false) String branchCodeFk
	) {
		try {
			List<RoomDTO> roomDTOList = roomService.pageToSearchedList(roomService.selectSearchedRoomsList(0, roomName, roomSubRoomsCount, roomCurrentStatus, branchCodeFk), roomName, roomSubRoomsCount, roomCurrentStatus, branchCodeFk);
			Map<String, Object> result = roomService.createRoomsExcelFile(roomDTOList);

			return ResponseEntity
				.ok()
				.headers((HttpHeaders)result.get("headers"))
				.body(new InputStreamResource((ByteArrayInputStream)result.get("result")));

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
