package org.iot.hotelitybackend.employee.service;

import org.iot.hotelitybackend.employee.dto.EmployeeDTO;

import java.util.Map;

public interface EmployeeService {
    Map<String, Object> selectEmployeesList(int pageNum, String branchCode, Integer departmentCode, String employeeName);
    EmployeeDTO selectEmployeeByEmployeeCodePk(int employCode);
}
