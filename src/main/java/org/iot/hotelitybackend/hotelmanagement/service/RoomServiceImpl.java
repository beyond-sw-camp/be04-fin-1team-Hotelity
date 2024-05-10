package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomCategoryDTO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
			.peek(roomDTO -> roomDTO.setRoomCategoryDTO(
				mapper.map(roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()), RoomCategoryDTO.class)
			))
			.peek(roomDTO -> roomDTO.setBranchDTO(
				mapper.map(branchRepository.findById(roomDTO.getBranchCodeFk()), BranchDTO.class)
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
}
