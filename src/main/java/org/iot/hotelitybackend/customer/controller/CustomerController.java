package org.iot.hotelitybackend.customer.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.common.vo.CustomerCriteria;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.dto.SelectCustomerDTO;
import org.iot.hotelitybackend.customer.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.iot.hotelitybackend.common.util.ExcelType.CUSTOMER;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @GetMapping("/page")
    public ResponseEntity<ResponseVO> selectCustomersList(@ModelAttribute CustomerCriteria criteria) {
        Map<String, Object> customerPageInfo = customerService.selectCustomersList(criteria);

        ResponseVO response = ResponseVO.builder()
                .data(customerPageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/{customerCodePk}/customer")
    public ResponseEntity<SelectCustomerDTO> selectCustomerByCustomerCodePk(@PathVariable("customerCodePk") Integer customerCodePk){

        SelectCustomerDTO customer = customerService.selectCustomerByCustomerCodePk(customerCodePk);
        // ResponseCustomer responseCustomer = mapper.map(customer, ResponseCustomer.class);

        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PostMapping()
    public ResponseEntity<ResponseVO> insertCustomer(@RequestBody CustomerDTO customerDTO){
        Map<String, Object> customerPageInfo = customerService.insertCustomer(customerDTO);

        ResponseVO response = ResponseVO.builder()
            .data(customerPageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @DeleteMapping("/{customerCodePk}")
    public ResponseEntity<ResponseVO> deleteCustomerByCustomerCodePk (@PathVariable("customerCodePk") int customerCodePk){

        Map<String, Object> customerPageInfo = customerService.deleteCustomerByCustomerCodePk(customerCodePk);
            ResponseVO response = ResponseVO.builder()
            .data(customerPageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();
        System.out.println(customerPageInfo);

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @PostMapping("/excel/read")
    public ResponseEntity<ResponseVO> readExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }
        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Map<String, Object> excelTest = customerService.readExcel(workbook);

        ResponseVO response = ResponseVO.builder()
            .data(excelTest)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/excel/download")
    public ResponseEntity<InputStreamResource> downloadExcel(
        @RequestParam(required = false) Integer customerCodePk,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String customerEmail,
        @RequestParam(required = false) String customerPhoneNumber,
        @RequestParam(required = false) String customerEnglishName,
        @RequestParam(required = false) String customerAddress,
        @RequestParam(required = false) Integer customerInfoAgreement,
        @RequestParam(required = false) Integer customerStatus,
        @RequestParam(required = false) Date customerRegisteredDate,
        @RequestParam(required = false) Integer nationCodeFk,
        @RequestParam(required = false) String customerGender,
        @RequestParam(required = false) String nationName,
        @RequestParam(required = false) String customerType,
        @RequestParam(required = false) String membershipLevelName
    ){
        try{
            ByteArrayInputStream result = customerService.downloadExcel(
                customerCodePk, customerName, customerEmail, customerPhoneNumber, customerEnglishName,
                customerAddress, customerInfoAgreement, customerStatus, customerRegisteredDate, nationCodeFk,
                customerGender, nationName, customerType, membershipLevelName
            );

            // 파일명에 현재시간 넣기 위한 작업
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
            String time = dateFormat.format(calendar.getTime());

            // UTF8 로 인코딩 해줘야 파일명에 한글 들어갔을 때 오류 발생 안함
            String fileName = URLEncoder.encode(
                    CUSTOMER.getFileName() + "_" + time + ".xlsx", StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/vnd.ms-excel");
            headers.add("Content-Disposition", "attachment; filename=" + fileName);

            return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(result));

        } catch(Exception e){
            log.info(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
