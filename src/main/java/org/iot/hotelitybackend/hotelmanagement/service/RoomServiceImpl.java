package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

@Service
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
	public Map<String, Object> selectRoomsList(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Page<RoomEntity> roomEntityPage = roomRepository.findAll(pageable);
		List<RoomDTO> roomDTOList = roomEntityPage
			.stream()
			.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
			.peek(roomDTO -> roomDTO.setRoomName(
				mapper.map(roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()), RoomCategoryDTO.class).getRoomName()
			))
			.peek(roomDTO -> roomDTO.setBranchName(
				mapper.map(branchRepository.findById(roomDTO.getBranchCodeFk()), BranchDTO.class).getBranchName()
			))
			.toList();

		int totalPagesCount = roomEntityPage.getTotalPages();
		int currentPageIndex = roomEntityPage.getNumber();

		Map<String, Object> roomPageInfo = new HashMap<>();

		roomPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		roomPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		roomPageInfo.put(KEY_CONTENT, roomDTOList);

		return roomPageInfo;
	}


	@Override
	public Map<String, Object> selectSearchedRoomsList(int pageNum, String roomName, Integer roomSubRoomsCount, String roomCurrentStatus, String branchCodeFk) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Specification<RoomEntity> spec = (root, query, criteriaBuilder) -> null;

		Map<String, Object> roomPageInfo = new HashMap<>();

		// 1. 객실 카테고리별 객실 필터링
		Integer roomCategoryCodeFk = null;
		if (!roomName.isEmpty()) {

			// 1. roomName 에 해당하는 roomCategoryCodeFk 찾기
			RoomCategoryEntity roomCategoryEntity = roomCategoryRepository.findByRoomName(roomName);
			if (roomCategoryEntity != null) {
				roomCategoryCodeFk = roomCategoryEntity.getRoomCategoryCodePk();
				spec = spec.and(RoomSpecification.equalsRoomCategoryCodeFk(roomCategoryCodeFk));
			} else {
				roomPageInfo.put("message", "다시 검색하세요");
				return roomPageInfo;
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

		Page<RoomEntity> roomEntityPage = roomRepository.findAll(spec, pageable);
		List<RoomDTO> roomDTOList = roomEntityPage
			.stream()
			.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
			.peek(roomDTO -> {
				RoomCategoryEntity foundRoomCategoryEntity = roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()).orElseThrow(IllegalArgumentException::new);
				roomDTO.setRoomName(foundRoomCategoryEntity.getRoomName());
				roomDTO.setRoomSubRoomsCount(foundRoomCategoryEntity.getRoomSubRoomsCount());
				roomDTO.setBranchName(branchRepository.findById(roomDTO.getBranchCodeFk()).orElseThrow(IllegalArgumentException::new).getBranchName());
			})
			.toList();

		int totalPagesCount = roomEntityPage.getTotalPages();
		int currentPageIndex = roomEntityPage.getNumber();

		roomPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		roomPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		roomPageInfo.put(KEY_CONTENT, roomDTOList);

		return roomPageInfo;
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

}
