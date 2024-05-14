package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.aggregate.AncillaryEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.AncillaryDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.AncillaryCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.AncillaryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AncillaryServiceImpl implements AncillaryService{

	private final AncillaryRepository ancillaryRepository;
	private final AncillaryCategoryRepository ancillaryCategoryRepository;
	private final BranchRepository branchRepository;
	private final ModelMapper mapper;

	@Autowired
	public AncillaryServiceImpl(AncillaryRepository ancillaryRepository, AncillaryCategoryRepository ancillaryCategoryRepository, BranchRepository branchRepository, ModelMapper mapper) {
		this.ancillaryRepository = ancillaryRepository;
		this.ancillaryCategoryRepository = ancillaryCategoryRepository;
		this.branchRepository = branchRepository;
		this.mapper = mapper;
	}

	@Override
	public Map<String, Object> selectAllFacilities(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Page<AncillaryEntity> ancillaryEntityPage = ancillaryRepository.findAll(pageable);
		List<AncillaryDTO> ancillaryDTOList = ancillaryEntityPage
			.stream()
			.map(ancillaryEntity -> mapper.map(ancillaryEntity, AncillaryDTO.class))
			.peek(ancillaryDTO -> {

				// 지점 이름 가져와 붙이기
				ancillaryDTO.setBranchName(
					branchRepository.findById(
						ancillaryDTO.getBranchCodeFk()
					).orElseThrow(IllegalArgumentException::new).getBranchName()
				);

				// 부대시설 카테고리 이름 가져와 붙이기
				ancillaryDTO.setAncillaryCategoryName(
					ancillaryCategoryRepository.findById(
						ancillaryDTO.getAncillaryCategoryCodeFk()
					).orElseThrow(IllegalArgumentException::new).getAncillaryCategoryName()
				);
			})
			.toList();

		int totalPagesCount = ancillaryEntityPage.getTotalPages();
		int currentPageIndex = ancillaryEntityPage.getNumber();

		Map<String, Object> ancillaryPageInfo = new HashMap<>();

		ancillaryPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		ancillaryPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		ancillaryPageInfo.put(KEY_CONTENT, ancillaryDTOList);

		return ancillaryPageInfo;
	}
}
