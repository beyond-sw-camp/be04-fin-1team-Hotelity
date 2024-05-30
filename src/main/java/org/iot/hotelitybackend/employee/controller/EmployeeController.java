package org.iot.hotelitybackend.employee.controller;

import lombok.extern.slf4j.Slf4j;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.service.AwsS3Service;
import org.iot.hotelitybackend.employee.service.EmployeeService;
import org.iot.hotelitybackend.employee.vo.RequestEmployee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_CONTENT;
import static org.iot.hotelitybackend.common.util.ExcelType.EMPLOYEE;
import static org.iot.hotelitybackend.common.util.ExcelUtil.createExcelFile;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final ModelMapper mapper;
    private final EmployeeService employeeService;
    private final AwsS3Service awsS3Service;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ModelMapper mapper, AwsS3Service awsS3Service) {
        this.mapper = mapper;
        this.employeeService = employeeService;
        this.awsS3Service = awsS3Service;
    }

    /* 조건별 전체 직원 페이지 리스트 조회 */
    @GetMapping("/page")
    public ResponseEntity<ResponseVO> selectEmployeesList(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer employeeCode,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String employeeAddress,
            @RequestParam(required = false) String employeePhoneNumber,
            @RequestParam(required = false) String employeeOfficePhoneNumber,
            @RequestParam(required = false) String employeeEmail,
            @RequestParam(required = false) String employeeResignStatus,
            @RequestParam(required = false) Integer permissionCode,
            @RequestParam(required = false) Integer positionCode,
            @RequestParam(required = false) Integer rankCode,
            @RequestParam(required = false) Integer departmentCode,
            @RequestParam(required = false) String branchCode,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) Integer sortBy
    ) {
        Map<String, Object> employPageInfo = employeeService.selectEmployeesList(
                pageNum, employeeCode, employeeName, employeeAddress, employeePhoneNumber,
                employeeOfficePhoneNumber, employeeEmail, employeeResignStatus,
                permissionCode, positionCode, rankCode, departmentCode, branchCode,
                orderBy, sortBy);

        ResponseVO response;

        if (!((List<EmployeeDTO>) employPageInfo.get(KEY_CONTENT)).isEmpty()) {
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

        ResponseVO response;

        if (employee == null) {
            response = ResponseVO.builder()
                    .resultCode(HttpStatus.BAD_REQUEST.value())
                    .message("이미 등록된 직원입니다.")
                    .build();
        } else {
            response = ResponseVO.builder()
                    .data(employee)
                    .resultCode(HttpStatus.CREATED.value())
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 직원 수정 */
    @PutMapping("/{employeeCode}")
    public ResponseEntity<ResponseVO> modifyEmployeeByEmployeeCodePk(
            @PathVariable("employeeCode") int employCode,
            @RequestBody RequestEmployee modifiedEmployInfo
    ) {
        EmployeeDTO modifiedEmployee = employeeService.modifyEmployeeByEmployeeCodePk(employCode, modifiedEmployInfo);

        ResponseVO response;

        if (modifiedEmployee != null) {
            response = ResponseVO.builder()
                    .data(modifiedEmployee)
                    .resultCode(HttpStatus.OK.value())
                    .build();
        } else {
            response = ResponseVO.builder().resultCode(HttpStatus.NO_CONTENT.value()).build();
        }

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    /* 직원 삭제 */
    @DeleteMapping("/{employeeCode}")
    public ResponseEntity<ResponseVO> deleteEmployeeByEmployeeCodePk(@PathVariable("employeeCode") int employCode) {

        /* -1: exception, 0: not exist, 1: success */
        int resultCode = employeeService.deleteEmployeeByEmployeeCodePk(employCode);
        ResponseVO response = switch (resultCode) {
            case -1 -> ResponseVO.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            case 0 -> ResponseVO.builder().resultCode(HttpStatus.NO_CONTENT.value()).build();
            case 1 -> ResponseVO.builder()
                    .resultCode(HttpStatus.OK.value())
                    .build();
            default -> throw new IllegalStateException("Unexpected value: " + resultCode);
        };

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    /* 직원 리스트 엑셀 파일 다운로드 */
    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> downloadEmployeeList(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer employeeCode,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String employeeAddress,
            @RequestParam(required = false) String employeePhoneNumber,
            @RequestParam(required = false) String employeeOfficePhoneNumber,
            @RequestParam(required = false) String employeeEmail,
            @RequestParam(required = false) String employeeResignStatus,
            @RequestParam(required = false) Integer permissionCode,
            @RequestParam(required = false) Integer positionCode,
            @RequestParam(required = false) Integer rankCode,
            @RequestParam(required = false) Integer departmentCode,
            @RequestParam(required = false) String branchCode,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) Integer sortBy
    ) {
        Map<String, Object> employeeList = employeeService.selectEmployeesList(
                pageNum, employeeCode, employeeName, employeeAddress, employeePhoneNumber,
                employeeOfficePhoneNumber, employeeEmail, employeeResignStatus,
                permissionCode, positionCode, rankCode, departmentCode, branchCode,
                orderBy, sortBy);

        try {
            Map<String, Object> result = createExcelFile(
                    (List<EmployeeDTO>) employeeList.get(KEY_CONTENT),
                    EMPLOYEE.getFileName(),
                    EMPLOYEE.getHeaderStrings()
            );

            return ResponseEntity
                    .ok()
                    .headers((HttpHeaders) result.get("headers"))
                    .body(new InputStreamResource((ByteArrayInputStream) result.get("result")));

        } catch (IOException | IllegalAccessException e) {
            log.info(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /* S3 이미지 업로드 */
    @PostMapping("/{employeeCode}/image")
    public ResponseEntity<?> uploadImageToS3(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable("employeeCode") int employCode
    ) {
        String imgUrl = awsS3Service.upload(image, employCode);

        if (imgUrl == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ResponseVO.builder().data(imgUrl).resultCode(HttpStatus.OK.value()).build());
    }

    /* S3 이미지 삭제 */
    @DeleteMapping("/{employeeCode}/image")
    public ResponseEntity<?> deleteImageFromS3(
            @RequestParam String imageAddress,
            @PathVariable("employeeCode") int employCode
    ) {
        boolean result = awsS3Service.deleteImageFromS3(imageAddress, employCode);

        if (!result) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ResponseVO.builder().resultCode(HttpStatus.OK.value()).build());
    }
}
