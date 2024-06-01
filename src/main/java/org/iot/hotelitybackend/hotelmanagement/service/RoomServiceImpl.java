package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
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
import org.apache.poi.ss.usermodel.CellType;
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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
		this.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		this.mapper.typeMap(RoomEntity.class, RoomDTO.class)
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getRoomName(),
				RoomDTO::setRoomName
			))
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getRoomSubRoomsCount(),
				RoomDTO::setRoomSubRoomsCount
			))
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getRoomPrice(),
				RoomDTO::setRoomPrice
			))
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getRoomCapacity(),
				RoomDTO::setRoomCapacity
			))
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getRoomBathroomCount(),
				RoomDTO::setRoomBathroomCount
			))
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getRoomSpecificInfo(),
				RoomDTO::setRoomSpecificInfo
			));
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
		Integer roomSubRoomsCount,
		Integer minPrice,
		Integer maxPrice,
		Integer roomPrice,
		Integer roomCapacity,
		Integer roomBathroomCount,
		String roomSpecificInfo,
		String orderBy,
		Integer sortBy
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

		// 1-9. 객실 가격별 객실 필터링
		if (minPrice != null && maxPrice != null) {
			spec = spec.and(RoomSpecification.isRoomPriceBetween(minPrice, maxPrice));
		}

		// 2. 위에서 구성한 Specification 을 적용하여 findAll
		List<RoomDTO> roomDTOList;

		// 2-1. 페이징처리 할 때
		if (pageNum != null) {
			Pageable pageable;

			if (orderBy != null && !orderBy.isEmpty() && sortBy != null) {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, sortBy == 1
					? Sort.by(orderBy).ascending()
					: Sort.by(orderBy).descending());
			} else {
				pageable = PageRequest.of(pageNum, PAGE_SIZE);
			}
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
			List<RoomEntity> roomEntityList;
			if (orderBy != null && !orderBy.isEmpty() && sortBy != null) {
				roomEntityList = roomRepository.findAll(spec, sortBy == 1 ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending());
			} else {
				roomEntityList = roomRepository.findAll(spec);
			}

			roomDTOList = setDTOList(roomEntityList);
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

	private List<RoomDTO> setDTOList(List<RoomEntity> roomEntityList) {
		return roomEntityList
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


}
