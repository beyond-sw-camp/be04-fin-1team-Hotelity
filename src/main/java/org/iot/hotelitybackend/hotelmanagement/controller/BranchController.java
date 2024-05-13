package org.iot.hotelitybackend.hotelmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.service.BranchService;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistBranch;
import org.iot.hotelitybackend.hotelmanagement.vo.ResponseBranch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public ResponseEntity<ResponseVO> selectAllBranches(@RequestParam int pageNum) {
		Map<String, Object> branchPageInfo = branchService.selectAllBranches(pageNum);

		ResponseVO response = ResponseVO.builder()
			.data(branchPageInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	@PostMapping("/branches")
	public ResponseEntity<ResponseVO> registBranch(@RequestBody RequestRegistBranch requestRegistBranch) {
		Map<String, Object> registeredBranch = branchService.registBranch(requestRegistBranch);
		ResponseVO response = ResponseVO.builder()
			.data(registeredBranch)
			.resultCode(HttpStatus.CREATED.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
