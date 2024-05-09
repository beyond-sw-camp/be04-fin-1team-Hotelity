package org.iot.hotelitybackend.hotelmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.service.BranchService;
import org.iot.hotelitybackend.hotelmanagement.vo.ResponseBranch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel-management")
public class BranchController {

	private final BranchService branchService;
	private final ModelMapper mapper;

	@Autowired
	public BranchController(BranchService branchService, ModelMapper mapper) {
		this.branchService = branchService;
		this.mapper = mapper;
	}

	@GetMapping("/branches")
	public ResponseEntity<Map<String, Object>> selectAllBranches() {
		List<BranchDTO> branchDTOList = branchService.selectAllBranches();
		Map<String, Object> result = new HashMap<>();

		List<ResponseBranch> responseBranchList = branchDTOList
			.stream()
			.map(BranchDTO -> mapper.map(BranchDTO, ResponseBranch.class))
			.toList();

		result.put("data", responseBranchList);
		if (branchDTOList != null) {
			result.put("resultCode", HttpStatus.OK);
			result.put("message", "조회 성공");
		} else {
			result.put("resultCode", HttpStatus.NOT_FOUND);
			result.put("message", "조회 실패");
		}

		return ResponseEntity.status((HttpStatusCode)result.get("resultCode")).body(result);
	}
}
