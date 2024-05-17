package org.iot.hotelitybackend.employee.service;

import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.vo.RequestEmployee;

import java.util.Map;

public interface EmployeeService {
    Map<String, Object> selectEmployeesList(int pageNum, String branchCode, Integer departmentCode, String employeeName);

    EmployeeDTO selectEmployeeByEmployeeCodePk(int employCode);

    EmployeeDTO registEmployee(EmployeeDTO newEmployee);

    EmployeeDTO modifyEmployeeByEmployeeCodePk(int employCode, RequestEmployee modifiedEmployInfo);

    int deleteEmployeeByEmployeeCodePk(int employCode);
}
