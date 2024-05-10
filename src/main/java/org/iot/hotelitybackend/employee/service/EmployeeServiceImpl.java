package org.iot.hotelitybackend.employee.service;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
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
import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(ModelMapper mapper, EmployeeRepository employeeRepository) {
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Map<String, Object> selectEmployeesList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<EmployeeEntity> employeePage = employeeRepository.findAll(pageable);
        List<EmployeeDTO> employeeDTOList =
                employeePage.stream().map(employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class)).toList();

        int totalPagesCount = employeePage.getTotalPages();
        int currentPageIndex = employeePage.getNumber();

        Map<String, Object> employeePageInfo = new HashMap<>();

        employeePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        employeePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        employeePageInfo.put(KEY_CONTENT, employeeDTOList);

        return employeePageInfo;
    }
}
