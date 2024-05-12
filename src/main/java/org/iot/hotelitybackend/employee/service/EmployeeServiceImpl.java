package org.iot.hotelitybackend.employee.service;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.employee.dto.*;
import org.iot.hotelitybackend.employee.repository.*;
import org.iot.hotelitybackend.hotelmanagement.dto.BranchDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final PermissionRepository permissionRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeServiceImpl(
            ModelMapper mapper,
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository,
            DepartmentRepository departmentRepository,
            PermissionRepository permissionRepository,
            RankRepository rankRepository,
            PositionRepository positionRepository
    ) {
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.departmentRepository = departmentRepository;
        this.permissionRepository = permissionRepository;
        this.rankRepository = rankRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public Map<String, Object> selectEmployeesList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<EmployeeEntity> employeePage = employeeRepository.findAll(pageable);
        List<EmployeeDTO> employeeDTOList = employeePage
                .stream()
                .map(employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class))
                .peek(employeeDTO -> employeeDTO.setNameOfBranch(mapper.map(
                        branchRepository.findById(employeeDTO.getBranchCodeFk()), BranchDTO.class).getBranchName()
                ))
                .peek(employeeDTO -> employeeDTO.setNameOfRank(mapper.map(
                        rankRepository.findById(employeeDTO.getRankCodeFk()), RankDTO.class).getRankName()
                ))
                .peek(employeeDTO -> employeeDTO.setNameOfPosition(mapper.map(
                        positionRepository.findById(employeeDTO.getPositionCodeFk()), PositionDTO.class).getPositionName()
                ))
                .peek(employeeDTO -> employeeDTO.setNameOfPermission(mapper.map(
                        permissionRepository.findById(employeeDTO.getPermissionCodeFk()), PermissionDTO.class).getPermissionName()
                ))
                .peek(employeeDTO -> employeeDTO.setNameOfDepartment(mapper.map(
                        departmentRepository.findById(employeeDTO.getDepartmentCodeFk()), DepartmentDTO.class).getDepartmentName()
                ))
                .toList();

/*
List<RoomDTO> roomDTOList = roomEntityPage
			.stream()
			.map(roomEntity -> mapper.map(roomEntity, RoomDTO.class))
			.peek(roomDTO -> roomDTO.setRoomName(
				mapper.map(roomCategoryRepository.findById(roomDTO.getRoomCategoryCodeFk()), RoomCategoryDTO.class).getRoomName()
			))
			.peek(roomDTO -> roomDTO.setBranchName(
				mapper.map(branchRepository.findById(roomDTO.getBranchCodeFk()), BranchDTO.class).getBranchName()
			))
			.toList();
 */

        int totalPagesCount = employeePage.getTotalPages();
        int currentPageIndex = employeePage.getNumber();

        Map<String, Object> employeePageInfo = new HashMap<>();

        employeePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        employeePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        employeePageInfo.put(KEY_CONTENT, employeeDTOList);

        return employeePageInfo;
    }

    @Override
    public EmployeeDTO selectEmployeeByEmployeeCodePk(int employCode) {

        EmployeeEntity employeeEntity =
                employeeRepository.findById(employCode).orElse(null);

        if (employeeEntity != null) {
            return mapper.map(employeeEntity, EmployeeDTO.class);
        }

        return null;
    }
}
