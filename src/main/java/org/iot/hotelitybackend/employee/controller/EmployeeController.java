package org.iot.hotelitybackend.employee.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.service.EmployeeService;
import org.iot.hotelitybackend.employee.vo.RequestEmployee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_TOTAL_PAGES_COUNT;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final ModelMapper mapper;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ModelMapper mapper) {
        this.mapper = mapper;
        this.employeeService = employeeService;
    }

    /* 조건별 전체 직원 페이지 리스트 조회 */
    @GetMapping("/page")
    public ResponseEntity<ResponseVO> selectEmployeesList(
            @RequestParam int pageNum,
            @RequestParam(required = false) String branchCode,
            @RequestParam(required = false) Integer departmentCode,
            @RequestParam(required = false) String employeeName
    ) {
        Map<String, Object> employPageInfo =
                employeeService.selectEmployeesList(pageNum, branchCode, departmentCode, employeeName);

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

    /* 직원 코드로 직원 조회 */
    @GetMapping("/{employeeCode}")
    public ResponseEntity<ResponseVO> selectEmployeeByEmployeeCodePk(@PathVariable("employeeCode") int employCode) {
        EmployeeDTO selectedEmployee = employeeService.selectEmployeeByEmployeeCodePk(employCode);

        ResponseVO response;

        if (selectedEmployee != null) {
            response = ResponseVO.builder()
                    .data(selectedEmployee)
                    .resultCode(HttpStatus.OK.value())
                    .build();
        } else {
            response = ResponseVO.builder().resultCode(HttpStatus.NO_CONTENT.value()).build();
        }

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    /* 직원 등록 */
    @PostMapping("/")
    public ResponseEntity<ResponseVO> registEmployee(@RequestBody RequestEmployee newEmployee) {
        EmployeeDTO newEmployeeDTO = mapper.map(newEmployee, EmployeeDTO.class);
        EmployeeDTO employee = employeeService.registEmployee(newEmployeeDTO);

        ResponseVO response = ResponseVO.builder()
                .data(employee)
                .resultCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
