package org.iot.hotelitybackend.hotelmanagement.controller;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;
import static org.iot.hotelitybackend.common.util.ExcelType.ROOM;
import static org.iot.hotelitybackend.common.util.ExcelUtil.createExcelFile;

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

	@GetMapping("/rooms")
	public ResponseEntity<ResponseVO> selectSearchedRoomsList(
		@RequestParam(required = false) Integer pageNum,
		@RequestParam(required = false) String roomCodePk,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) Integer roomNumber,
		@RequestParam(required = false) String roomName,
		@RequestParam(required = false) String roomCurrentStatus,
		@RequestParam(required = false) Float roomDiscountRate,
		@RequestParam(required = false) String roomView,
		@RequestParam(required = false) Integer roomSubRoomsCount,
		@RequestParam(required = false) Integer minPrice,
		@RequestParam(required = false) Integer maxPrice,
		@RequestParam(required = false) Integer roomPrice,
		@RequestParam(required = false) Integer roomCapacity,
		@RequestParam(required = false) Integer roomBathroomCount,
		@RequestParam(required = false) String roomSpecificInfo,
		@RequestParam(required = false) String roomLevelName,
		@RequestParam(required = false) String orderBy,
		@RequestParam(required = false) Integer sortBy
	) {

		Map<String, Object> roomListInfo = roomService.selectSearchedRoomsList(
			pageNum, roomCodePk, branchCodeFk, roomNumber, roomName, roomCurrentStatus, roomDiscountRate, roomView, roomSubRoomsCount, minPrice, maxPrice, roomPrice, roomCapacity, roomBathroomCount, roomSpecificInfo, roomLevelName, orderBy, sortBy);

		ResponseVO response = ResponseVO.builder()
			.data(roomListInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("/rooms/{roomCodePk}")
	public ResponseEntity<ResponseVO> selectRoomInfo(@PathVariable("roomCodePk") String roomCodePk) {
		Map<String, Object> roomInfo = roomService.selectRoomInfo(roomCodePk);

		ResponseVO response = ResponseVO.builder()
			.data(roomInfo)
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
	public ResponseEntity<ResponseVO> deleteRoom(@PathVariable("roomCodePk") String roomCodePk) {
		Map<String, Object> deleteRoom = roomService.deleteRoom(roomCodePk);
		ResponseVO response = ResponseVO.builder()
			.data(deleteRoom)
			.resultCode(HttpStatus.NO_CONTENT.value())
			.message("삭제 성공")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@GetMapping("rooms/excel/download")
	public ResponseEntity<InputStreamResource> downloadSearchedRoomsAsExcel(
		@RequestParam(required = false) Integer pageNum,
		@RequestParam(required = false) String roomCodePk,
		@RequestParam(required = false) String branchCodeFk,
		@RequestParam(required = false) Integer roomNumber,
		@RequestParam(required = false) String roomName,
		@RequestParam(required = false) String roomCurrentStatus,
		@RequestParam(required = false) Float roomDiscountRate,
		@RequestParam(required = false) String roomView,
		@RequestParam(required = false) Integer roomSubRoomsCount,
		@RequestParam(required = false) Integer minPrice,
		@RequestParam(required = false) Integer maxPrice,
		@RequestParam(required = false) Integer roomPrice,
		@RequestParam(required = false) Integer roomCapacity,
		@RequestParam(required = false) Integer roomBathroomCount,
		@RequestParam(required = false) String roomSpecificInfo,
		@RequestParam(required = false) String roomLevelName,
		@RequestParam(required = false) String orderBy,
		@RequestParam(required = false) Integer sortBy
	) {
		try {

			// 조회해서 DTO 리스트 가져오기
			Map<String, Object> roomListInfo = roomService.selectSearchedRoomsList(
				pageNum, roomCodePk, branchCodeFk, roomNumber, roomName, roomCurrentStatus, roomDiscountRate, roomView, roomSubRoomsCount, minPrice, maxPrice, roomPrice, roomCapacity, roomBathroomCount, roomSpecificInfo, roomLevelName, orderBy, sortBy);

			// 엑셀 시트와 파일 만들기
			Map<String, Object> result = createExcelFile(
					(List<RoomDTO>)roomListInfo.get(KEY_CONTENT),
					ROOM.getFileName(),
					ROOM.getHeaderStrings()
			);

			return ResponseEntity
				.ok()
				.headers((HttpHeaders)result.get("headers"))
				.body(new InputStreamResource((ByteArrayInputStream)result.get("result")));

		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
