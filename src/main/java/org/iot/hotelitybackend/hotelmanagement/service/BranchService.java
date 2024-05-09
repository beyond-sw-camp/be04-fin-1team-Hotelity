package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.List;

import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;

public interface BranchService {
	List<BranchDTO> selectAllBranches();
}
