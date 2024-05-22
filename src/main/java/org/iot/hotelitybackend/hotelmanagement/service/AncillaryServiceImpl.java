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
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyFacility;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistFacility;
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

	@Override
	public Map<String, Object> registFacility(RequestRegistFacility requestRegistFacility) {
		AncillaryEntity ancillaryEntity = AncillaryEntity.builder()
				.ancillaryName(requestRegistFacility.getAncillaryName())
				.branchCodeFk(requestRegistFacility.getBranchCodeFk())
				.ancillaryLocation(requestRegistFacility.getAncillaryLocation())
				.ancillaryOpenTime(requestRegistFacility.getAncillaryOpenTime())
				.ancillaryCloseTime(requestRegistFacility.getAncillaryCloseTime())
				.ancillaryPhoneNumber(requestRegistFacility.getAncillaryPhoneNumber())
				.ancillaryCategoryCodeFk(requestRegistFacility.getAncillaryCategoryCodeFk())
				.build();

		Map<String, Object> registFacilityInfo = new HashMap<>();

		registFacilityInfo.put(KEY_CONTENT, mapper.map(ancillaryRepository.save(ancillaryEntity), AncillaryDTO.class));

		return registFacilityInfo;
	}

	@Override
	public Map<String, Object> modifyFacilityInfo(RequestModifyFacility requestModifyFacility, int ancillaryCodePk) {
		AncillaryEntity ancillaryEntity = AncillaryEntity.builder()
				.ancillaryCodePk(ancillaryCodePk)
				.ancillaryName(requestModifyFacility.getAncillaryName())
				.branchCodeFk(requestModifyFacility.getBranchCodeFk())
				.ancillaryLocation(requestModifyFacility.getAncillaryLocation())
				.ancillaryOpenTime(requestModifyFacility.getAncillaryOpenTime())
				.ancillaryCloseTime(requestModifyFacility.getAncillaryCloseTime())
				.ancillaryPhoneNumber(requestModifyFacility.getAncillaryPhoneNumber())
				.ancillaryCategoryCodeFk(requestModifyFacility.getAncillaryCategoryCodeFk())
				.build();

		Map<String, Object> modifyFacilityInfo = new HashMap<>();

		modifyFacilityInfo.put(KEY_CONTENT, mapper.map(ancillaryRepository.save(ancillaryEntity), AncillaryDTO.class));

		return modifyFacilityInfo;
	}

	@Override
	public Map<String, Object> deleteFacilityInfo(int ancillaryCodePk) {
		Map<String, Object> deleteFacilityInfo = new HashMap<>();

		if (ancillaryRepository.existsById(ancillaryCodePk)) {
			ancillaryRepository.deleteById(ancillaryCodePk);
		} else {
			System.out.println("해당하는 부대 시설을 찾을 수 없습니다.");
		}

		return deleteFacilityInfo;
	}
}
