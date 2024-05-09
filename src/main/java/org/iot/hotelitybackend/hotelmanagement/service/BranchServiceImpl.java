package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<BranchDTO> selectAllBranches() {
		List<BranchEntity> branchEntityList = branchRepository.findAll();

		return branchEntityList
			.stream()
			.map(BranchEntity -> mapper.map(BranchEntity, BranchDTO.class))
			.toList();
	}
}
