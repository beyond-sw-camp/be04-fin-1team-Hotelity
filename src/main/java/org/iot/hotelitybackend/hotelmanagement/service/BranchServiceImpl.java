package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyBranch;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistBranch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchServiceImpl implements BranchService{

	private final BranchRepository branchRepository;
	private final ModelMapper mapper;

	@Autowired
	public BranchServiceImpl(BranchRepository branchRepository, ModelMapper mapper) {
		this.branchRepository = branchRepository;
		this.mapper = mapper;
	}

	@Override
	public Map<String, Object> selectAllBranches(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Page<BranchEntity> branchEntityPage = branchRepository.findAll(pageable);
		List<BranchDTO> branchDTOList = branchEntityPage
			.stream()
			.map(branchEntity -> mapper.map(branchEntity, BranchDTO.class))
			.toList();

		int totalPagesCount = branchEntityPage.getTotalPages();
		int currentPageIndex = branchEntityPage.getNumber();

		Map<String, Object> branchPageInfo = new HashMap<>();

		branchPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		branchPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		branchPageInfo.put(KEY_CONTENT, branchDTOList);

		return branchPageInfo;
	}

	@Transactional
	@Override
	public Map<String, Object> registBranch(RequestRegistBranch requestRegistBranch) {
		BranchEntity branchEntity = BranchEntity.builder()
			.branchCodePk(requestRegistBranch.getBranchCodePk())
			.branchName(requestRegistBranch.getBranchName())
			.branchAddress(requestRegistBranch.getBranchAddress())
			.branchPhoneNumber(requestRegistBranch.getBranchPhoneNumber())
			.build();

		Map<String, Object> registeredBranchInfo = new HashMap<>();
		registeredBranchInfo.put(KEY_CONTENT, mapper.map(branchRepository.save(branchEntity), BranchDTO.class));
		return registeredBranchInfo;
	}

	@Transactional
	@Override
	public Map<String, Object> modifyBranchInfo(RequestModifyBranch requestModifyBranch, String branchCodePk) {
		BranchEntity branchEntity = BranchEntity.builder()
			.branchCodePk(branchCodePk)
			.branchName(requestModifyBranch.getBranchName())
			.branchAddress(requestModifyBranch.getBranchAddress())
			.branchPhoneNumber(requestModifyBranch.getBranchPhoneNumber())
			.build();

		Map<String, Object> modifiedBranchInfo = new HashMap<>();
		modifiedBranchInfo.put(KEY_CONTENT, mapper.map(branchRepository.save(branchEntity), BranchDTO.class));
		return modifiedBranchInfo;
	}

	@Transactional
	@Override
	public Map<String, Object> deleteBranch(String branchCodePk) {

		Map<String, Object> deleteBranchInfo = new HashMap<>();
		try {
			branchRepository.deleteById(branchCodePk);
			deleteBranchInfo.put(KEY_CONTENT, "Content deleted successfully.");
		} catch (Exception e) {
			deleteBranchInfo.put(KEY_CONTENT, "Failed to delete content.");
		}
		return deleteBranchInfo;
	}
}
