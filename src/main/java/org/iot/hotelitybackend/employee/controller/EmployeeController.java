package org.iot.hotelitybackend.employee.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_TOTAL_PAGES_COUNT;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/page")
    public ResponseEntity<ResponseVO> selectEmployeesList(@RequestParam int pageNum) {
        Map<String, Object> employPageInfo = employeeService.selectEmployeesList(pageNum);

        ResponseVO response;

        if ((Integer) employPageInfo.get(KEY_TOTAL_PAGES_COUNT) != 0) {
            response = ResponseVO.builder()
                    .data(employPageInfo)
                    .resultCode(HttpStatus.OK.value())
                    .build();
        } else {
            response = ResponseVO.builder().resultCode(HttpStatus.NO_CONTENT.value()).build();
        }

        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
