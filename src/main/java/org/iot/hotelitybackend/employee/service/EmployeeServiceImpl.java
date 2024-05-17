package org.iot.hotelitybackend.employee.service;

import org.iot.hotelitybackend.employee.aggregate.*;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.*;
import org.iot.hotelitybackend.employee.vo.RequestEmployee;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;
    private final PositionRepository positionRepository;
    private final RankRepository rankRepository;
    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public EmployeeServiceImpl(
            ModelMapper mapper,
            EmployeeRepository employeeRepository,
            PermissionRepository permissionRepository,
            PositionRepository positionRepository,
            RankRepository rankRepository,
            DepartmentRepository departmentRepository,
            BranchRepository branchRepository
    ) {
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
        this.permissionRepository = permissionRepository;
        this.positionRepository = positionRepository;
        this.rankRepository = rankRepository;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;

        /* custom mapping */
        this.mapper.typeMap(EmployeeEntity.class, EmployeeDTO.class).addMappings(modelMapper -> {
            modelMapper.map(EmployeeEntity::getPermissionName, EmployeeDTO::setNameOfPermission);
            modelMapper.map(EmployeeEntity::getPositionName, EmployeeDTO::setNameOfPosition);
            modelMapper.map(EmployeeEntity::getRankName, EmployeeDTO::setNameOfRank);
            modelMapper.map(EmployeeEntity::getBranchName, EmployeeDTO::setNameOfBranch);
            modelMapper.map(EmployeeEntity::getDepartmentName, EmployeeDTO::setNameOfDepartment);
        });
    }

    @Override
    public Map<String, Object> selectEmployeesList(
            int pageNum, String branchCode, Integer departmentCode, String employeeName
    ) {
        Specification<EmployeeEntity> spec = (root, query, criteriaBuilder) -> null;

        if (branchCode != null && !branchCode.isEmpty()) {
            spec = spec.and(EmploySpecification.equalsBranch(branchCode));
        }

        if (departmentCode != null) {
            spec = spec.and(EmploySpecification.equalsDepartment(departmentCode));
        }

        if (employeeName != null && !employeeName.isEmpty()) {
            spec = spec.and(EmploySpecification.containsEmployeeName(employeeName));
        }

        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<EmployeeEntity> employeePage = employeeRepository.findAll(spec, pageable);

        List<EmployeeDTO> employeeDTOList = employeePage
                .stream()
                .map(employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class))
                .toList();

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

    @Override
    public EmployeeDTO registEmployee(EmployeeDTO newEmployee) {
        PermissionEntity permission =
                permissionRepository.findById(newEmployee.getPermissionCodeFk()).orElse(null);
        DepartmentEntity department =
                departmentRepository.findById(newEmployee.getDepartmentCodeFk()).orElse(null);
        PositionEntity position = positionRepository.findById(newEmployee.getPositionCodeFk()).orElse(null);
        BranchEntity branch = branchRepository.findById(newEmployee.getBranchCodeFk()).orElse(null);
        RankEntity rank = rankRepository.findById(newEmployee.getRankCodeFk()).orElse(null);

        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .employeeName(newEmployee.getEmployeeName())
                .employeeAddress(newEmployee.getEmployeeAddress())
                .employeePhoneNumber(newEmployee.getEmployeePhoneNumber())
                .employeeOfficePhoneNumber(newEmployee.getEmployeeOfficePhoneNumber())
                .employeeEmail(newEmployee.getEmployeeEmail())
                .employeeSystemPassword(newEmployee.getEmployeeSystemPassword())
                .employeeResignStatus("N")
                .employeeProfileImageLink(newEmployee.getEmployeeProfileImageLink())
                .employPermission(permission)
                .employPosition(position)
                .employRank(rank)
                .employDepartment(department)
                .employBranch(branch)
                .build();

        EmployeeEntity createdEmployeeEntity = employeeRepository.save(employeeEntity);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeCodePk(createdEmployeeEntity.getEmployeeCodePk());
        employeeDTO.setEmployeeName(createdEmployeeEntity.getEmployeeName());
        employeeDTO.setEmployeeAddress(createdEmployeeEntity.getEmployeeAddress());
        employeeDTO.setEmployeePhoneNumber(createdEmployeeEntity.getEmployeePhoneNumber());
        employeeDTO.setEmployeeOfficePhoneNumber(createdEmployeeEntity.getEmployeeOfficePhoneNumber());
        employeeDTO.setEmployeeEmail(createdEmployeeEntity.getEmployeeEmail());
        employeeDTO.setEmployeeSystemPassword(createdEmployeeEntity.getEmployeeSystemPassword());
        employeeDTO.setEmployeeResignStatus(createdEmployeeEntity.getEmployeeResignStatus());
        employeeDTO.setEmployeeProfileImageLink(createdEmployeeEntity.getEmployeeProfileImageLink());

        employeeDTO.setPermissionCodeFk(createdEmployeeEntity.getPermission().getPermissionCodePk());
        employeeDTO.setPositionCodeFk(createdEmployeeEntity.getPosition().getPositionCodePk());
        employeeDTO.setRankCodeFk(createdEmployeeEntity.getRank().getRankCodePk());
        employeeDTO.setDepartmentCodeFk(createdEmployeeEntity.getDepartment().getDepartmentCodePk());
        employeeDTO.setBranchCodeFk(createdEmployeeEntity.getBranch().getBranchCodePk());

        employeeDTO.setNameOfPermission(createdEmployeeEntity.getPermissionName());
        employeeDTO.setNameOfPosition(createdEmployeeEntity.getPositionName());
        employeeDTO.setNameOfRank(createdEmployeeEntity.getRankName());
        employeeDTO.setNameOfDepartment(createdEmployeeEntity.getDepartmentName());
        employeeDTO.setNameOfBranch(createdEmployeeEntity.getBranchName());

        return employeeDTO;
    }
}
