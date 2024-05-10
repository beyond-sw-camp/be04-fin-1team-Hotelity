package org.iot.hotelitybackend.hotelmanagement.service;

import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;

public interface BranchService {

	Map<String, Object> selectAllBranches(int pageNum);
}
