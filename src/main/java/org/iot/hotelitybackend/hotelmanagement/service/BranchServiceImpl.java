package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
