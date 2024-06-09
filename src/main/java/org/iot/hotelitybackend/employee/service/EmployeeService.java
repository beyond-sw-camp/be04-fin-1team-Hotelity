package org.iot.hotelitybackend.employee.service;

import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.vo.EmployeeSearchCriteria;
import org.iot.hotelitybackend.employee.vo.RequestEmployee;

import java.util.Map;

public interface EmployeeService {
    EmployeeDTO selectEmployeeByEmployeeCodePk(int employCode);

    EmployeeDTO registEmployee(EmployeeDTO newEmployee);

    EmployeeDTO modifyEmployeeByEmployeeCodePk(int employCode, RequestEmployee modifiedEmployInfo);

    int deleteEmployeeByEmployeeCodePk(int employCode);

    Map<String, Object> selectEmployeesList(EmployeeSearchCriteria criteria);
}
