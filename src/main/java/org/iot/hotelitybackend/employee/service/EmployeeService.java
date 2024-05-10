package org.iot.hotelitybackend.employee.service;

import java.util.Map;

public interface EmployeeService {
    Map<String, Object> selectEmployeesList(int pageNum);
}
