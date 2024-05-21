package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomSpecification;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomCategoryDTO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyRoom;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;
	private final RoomCategoryRepository roomCategoryRepository;
	private final BranchRepository branchRepository;
	private final ModelMapper mapper;

	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository, RoomCategoryRepository roomCategoryRepository, BranchRepository branchRepository, ModelMapper mapper) {
		this.roomRepository = roomRepository;
		this.roomCategoryRepository = roomCategoryRepository;
		this.branchRepository = branchRepository;
		this.mapper = mapper;
	}

	@Override
	public Map<String, Object> selectSearchedRoomsList(
		Integer pageNum,
		String roomCodePk,
		String branchCodeFk,
		Integer roomNumber,
		String roomName,
		String roomCurrentStatus,
		Float roomDiscountRate,
		String roomView,
		Integer roomSubRoomsCount
	) {

		Specification<RoomEntity> spec = (root, query, criteriaBuilder) -> null;

		Map<String, Object> roomListInfo = new HashMap<>();

		// 1-1. 객실코드(PK) 기준으로 검색 (like)
		if (roomCodePk != null) {
			spec = spec.and(RoomSpecification.likeRoomCodePk(roomCodePk));
		}

		// 1-2. 지점별 객실 기준으로 필터링
		if (branchCodeFk != null) {
			spec = spec.and(RoomSpecification.equalsBranchCodeFk(branchCodeFk));
		}

		// 1-3. 객실호수(번호) 기준으로 검색
		if (roomNumber != null) {
			spec = spec.and(RoomSpecification.equalsRoomNumber(roomNumber));
		}

		// 1-4. 객실 카테고리 기준 객실 필터링
		Integer roomCategoryCodeFk = null;
		if (roomName != null) {

			// roomName 에 해당하는 roomCategoryCodeFk 찾기
			RoomCategoryEntity roomCategoryEntity = roomCategoryRepository.findByRoomName(roomName);
			if (roomCategoryEntity != null) {
				roomCategoryCodeFk = roomCategoryEntity.getRoomCategoryCodePk();
				spec = spec.and(RoomSpecification.equalsRoomCategoryCodeFk(roomCategoryCodeFk));
			} else {
				roomListInfo.put("message", "다시 검색하세요");
				return roomListInfo;
			}
		}

		// 1-5. 객실 현재 상태 기준 객실 필터링
		// roomCurrentStatus가 비어있지 않은 경우 Specification 추가
		if (roomCurrentStatus != null) {
			spec = spec.and(RoomSpecification.equalsRoomCurrentStatus(roomCurrentStatus));
		}

		// 1-6. 객실 할인율 기준 필터링
		if (roomDiscountRate != null) {
			spec = spec.and(RoomSpecification.equalsRoomDiscountRate(roomDiscountRate));
		}

		// 1-7. 객실 뷰 기준 필터링 (like)
		if (roomView != null) {
			spec = spec.and(RoomSpecification.likeRoomView(roomView));
		}

		// 1-8. 방 개수별 객실 필터링
		// roomSubRoomsCount가 null이 아닌 경우 Specification 추가
		if (roomSubRoomsCount != null) {
			List<RoomCategoryEntity> roomCategoryEntityList = roomCategoryRepository.findAllByRoomSubRoomsCount(roomSubRoomsCount);
			List<Specification<RoomEntity>> specs = roomCategoryEntityList.stream()
				.map(roomCategoryEntity -> RoomSpecification.equalsRoomCategoryCodeFk(roomCategoryEntity.getRoomCategoryCodePk()))
				.collect(Collectors.toList());
			spec = spec.and(buildOrPredicate(specs));
		}

		// 2. 위에서 구성한 Specification 을 적용하여 findAll
		List<RoomDTO> roomDTOList;

		// 2-1. 페이징처리 할 때
		if (pageNum != null) {
			Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
			Page<RoomEntity> roomEntityList = roomRepository.findAll(spec, pageable);

			roomDTOList = roomEntityList
				.stream()
				.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
				.peek(roomDTO -> {
					RoomCategoryEntity foundRoomCategoryEntity = roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()).orElseThrow(IllegalArgumentException::new);
					roomDTO.setRoomName(foundRoomCategoryEntity.getRoomName());
					roomDTO.setRoomSubRoomsCount(foundRoomCategoryEntity.getRoomSubRoomsCount());
					roomDTO.setBranchName(branchRepository.findById(roomDTO.getBranchCodeFk()).orElseThrow(IllegalArgumentException::new).getBranchName());
				})
				.toList();

			int totalPagesCount = roomEntityList.getTotalPages();
			int currentPageIndex = roomEntityList.getNumber();
			roomListInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
			roomListInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);

		// 2-2. 페이징 처리 안할 때
		} else {
			List<RoomEntity> roomEntityList = roomRepository.findAll(spec);
			roomDTOList = roomEntityList
				.stream()
				.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
				.peek(roomDTO -> {
					RoomCategoryEntity foundRoomCategoryEntity = roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()).orElseThrow(IllegalArgumentException::new);
					roomDTO.setRoomName(foundRoomCategoryEntity.getRoomName());
					roomDTO.setRoomSubRoomsCount(foundRoomCategoryEntity.getRoomSubRoomsCount());
					roomDTO.setBranchName(branchRepository.findById(roomDTO.getBranchCodeFk()).orElseThrow(IllegalArgumentException::new).getBranchName());
				})
				.toList();
		}

		roomListInfo.put(KEY_CONTENT, roomDTOList);

		return roomListInfo;
	}

	// Helper method to build OR predicate
	private Specification<RoomEntity> buildOrPredicate(List<Specification<RoomEntity>> specs) {
		return (root, query, criteriaBuilder) -> {
			Predicate[] predicates = specs.stream()
				.map(spec -> spec.toPredicate(root, query, criteriaBuilder))
				.toArray(Predicate[]::new);
			return criteriaBuilder.or(predicates);
		};
	}

	@Transactional
	@Override
	public Map<String, Object> modifyRoomInfo(RequestModifyRoom requestModifyRoom, String roomCodePk) {
		RoomEntity roomEntity = RoomEntity.builder()
			.roomCodePk(roomCodePk)
			.branchCodeFk(requestModifyRoom.getBranchCodeFk())
			.roomNumber(requestModifyRoom.getRoomNumber())
			.roomCategoryCodeFk(requestModifyRoom.getRoomCategoryCodeFk())
			.roomCurrentStatus(requestModifyRoom.getRoomCurrentStatus())
			.roomDiscountRate(requestModifyRoom.getRoomDiscountRate())
			.roomImageLink(requestModifyRoom.getRoomImageLink())
			.roomView(requestModifyRoom.getRoomView())
			.build();

		Map<String, Object> modifiedRoomInfo = new HashMap<>();
		modifiedRoomInfo.put(KEY_CONTENT, mapper.map(roomRepository.save(roomEntity), RoomDTO.class));
		return modifiedRoomInfo;
	}

	@Transactional
	@Override
	public Map<String, Object> deleteRoom(String roomCodePk) {

		Map<String, Object> deleteRoom = new HashMap<>();
		try {
			roomRepository.deleteById(roomCodePk);
			deleteRoom.put(KEY_CONTENT, "Content deleted successfully.");
		} catch (Exception e) {
			deleteRoom.put(KEY_CONTENT, "Failed to delete content.");
		}
		return deleteRoom;
	}

	@Override
	public List<RoomDTO> selectRoomsForExcel() {
		return roomRepository.findAll()
			.stream()
			.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
			.peek(roomDTO -> {
				RoomCategoryEntity foundRoomCategoryEntity = roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()).orElseThrow(IllegalArgumentException::new);
				roomDTO.setRoomName(foundRoomCategoryEntity.getRoomName());
				roomDTO.setRoomSubRoomsCount(foundRoomCategoryEntity.getRoomSubRoomsCount());
				roomDTO.setBranchName(branchRepository.findById(roomDTO.getBranchCodeFk()).orElseThrow(IllegalArgumentException::new).getBranchName());
			})
			.toList();
	}

	@Override
	public Map<String, Object> selectSearchedRoomsForExcel(String roomName, Integer roomSubRoomsCount,
		String roomCurrentStatus, String branchCodeFk) {

		Specification<RoomEntity> spec = (root, query, criteriaBuilder) -> null;

		Map<String, Object> roomInfo = new HashMap<>();

		// 1. 객실 카테고리별 객실 필터링
		Integer roomCategoryCodeFk = null;
		if (!roomName.isEmpty()) {

			// 1. roomName 에 해당하는 roomCategoryCodeFk 찾기
			RoomCategoryEntity roomCategoryEntity = roomCategoryRepository.findByRoomName(roomName);
			if (roomCategoryEntity != null) {
				roomCategoryCodeFk = roomCategoryEntity.getRoomCategoryCodePk();
				spec = spec.and(RoomSpecification.equalsRoomCategoryCodeFk(roomCategoryCodeFk));
			} else {
				roomInfo.put("message", "다시 검색하세요");
				return roomInfo;
			}
		}

		// 2. 방 개수별 객실 필터링
		// roomSubRoomsCount가 null이 아닌 경우 Specification 추가
		if (roomSubRoomsCount != null) {
			List<RoomCategoryEntity> roomCategoryEntityList = roomCategoryRepository.findAllByRoomSubRoomsCount(roomSubRoomsCount);
			List<Specification<RoomEntity>> specs = roomCategoryEntityList.stream()
				.map(roomCategoryEntity -> RoomSpecification.equalsRoomCategoryCodeFk(roomCategoryEntity.getRoomCategoryCodePk()))
				.collect(Collectors.toList());
			spec = spec.and(buildOrPredicate(specs));
		}

		// 3. 현재 상태별 객실 필터링
		// roomCurrentStatus가 비어있지 않은 경우 Specification 추가
		if (!roomCurrentStatus.isEmpty()) {
			spec = spec.and(RoomSpecification.equalsRoomCurrentStatus(roomCurrentStatus));
		}

		// 4. 지점별 객실 필터링
		if (!branchCodeFk.isEmpty()) {
			spec = spec.and(RoomSpecification.equalsBranchCodeFk(branchCodeFk));
		}

		List<RoomEntity> roomEntityList = roomRepository.findAll(spec);

		List<RoomDTO> roomDTOList = roomEntityList
			.stream()
			.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
			.peek(roomDTO -> {
				RoomCategoryEntity foundRoomCategoryEntity = roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()).orElseThrow(IllegalArgumentException::new);
				roomDTO.setRoomName(foundRoomCategoryEntity.getRoomName());
				roomDTO.setRoomSubRoomsCount(foundRoomCategoryEntity.getRoomSubRoomsCount());
				roomDTO.setBranchName(branchRepository.findById(roomDTO.getBranchCodeFk()).orElseThrow(IllegalArgumentException::new).getBranchName());
			})
			.toList();

		roomInfo.put("message", "다중 조건 조회 성공");
		roomInfo.put(KEY_CONTENT, roomDTOList);

		return roomInfo;
	}

	// @Override
	// public List<RoomDTO> pageToList(
	// 	Map<String, Object> roomPageInfo
	// ) {
	// 	List<RoomDTO> joined = new ArrayList<>();
	// 	for (int i = 0; i < (int)roomPageInfo.get(KEY_TOTAL_PAGES_COUNT); i++) {
	// 		joined.addAll((List<RoomDTO>)selectRoomsList(i).get(KEY_CONTENT));
	// 	}
	// 	return joined;
	// }
	//
	// @Override
	// public List<RoomDTO> pageToSearchedList(
	// 	Map<String, Object> roomPageInfo,
	// 	String roomName, Integer roomSubRoomsCount, String roomCurrentStatus, String branchCodeFk
	// ) {
	// 	List<RoomDTO> joined = new ArrayList<>();
	// 	for (int i = 0; i < (int)roomPageInfo.get(KEY_TOTAL_PAGES_COUNT); i++) {
	// 		joined.addAll((List<RoomDTO>)selectSearchedRoomsList(i, roomName, roomSubRoomsCount, roomCurrentStatus, branchCodeFk).get(KEY_CONTENT));
	// 	}
	// 	return joined;
	// }

	@Override
	public Map<String, Object> createRoomsExcelFile(List<RoomDTO> roomDTOList) throws
		IOException,
		NoSuchFieldException,
		IllegalAccessException {

		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Sheet sheet = workbook.createSheet("객실");

		createDashboardSheet(roomDTOList, sheet, headerCellStyle);

		workbook.write(out);
		log.info("[ReportService:getExcel] create Excel list done. row count:[{}]", roomDTOList.size());

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String time = dateFormat.format(calendar.getTime());
		String fileName = "Rooms_"+ time +".xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/vnd.ms-excel");
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		Map<String, Object> result = new HashMap<>();
		result.put("result", new ByteArrayInputStream(out.toByteArray()));
		result.put("fileName", fileName);
		result.put("headers", headers);
		return result;
	}

	// 첫번째 인자인 List<RoomDTO> 만 바꿔서 쓰면 됨
	private void createDashboardSheet(List<RoomDTO> roomDTOList, Sheet sheet, CellStyle headerCellStyle) throws
		NoSuchFieldException, IllegalAccessException {
		Row headerRow = sheet.createRow(0);
		RoomDTO roomDTO = new RoomDTO();
		int idx1 = 0;
		Cell headerCell;
		List<String> headerStrings = new ArrayList<>();
		for (Field field : roomDTO.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			headerStrings.add(fieldName);
			headerCell = headerRow.createCell(idx1++);
			headerCell.setCellValue(fieldName);
			headerCell.setCellStyle(headerCellStyle);
		}

		Row bodyRow;
		Cell bodyCell;
		int idx2 = 1;
		for (RoomDTO roomDTOIter : roomDTOList) {
			bodyRow = sheet.createRow(idx2++);
			int idx3 = 0;
			for (String headerString : headerStrings) {
				Field field = roomDTOIter.getClass().getDeclaredField(headerString);
				field.setAccessible(true);
				bodyCell = bodyRow.createCell(idx3);
				bodyCell.setCellValue(String.valueOf(field.get(roomDTOIter)));
				idx3++;
			}
		}

		for (int i = 0; i < headerStrings.size(); i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + (short)1024);
		}
	}
}
