package org.iot.hotelitybackend.customer.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.dto.SelectCustomerDTO;
import org.iot.hotelitybackend.customer.service.CustomerService;
import org.iot.hotelitybackend.customer.vo.ResponseCustomer;
import org.iot.hotelitybackend.sales.dto.MembershipDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @GetMapping("/page")
    public ResponseEntity<ResponseVO> selectCustomersList(
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
        @RequestParam(required = false) String membershipLevelName,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) Integer sortBy,
        @RequestParam Integer pageNum) {
        Map<String, Object> customerPageInfo = customerService.selectCustomersList(
            customerCodePk, customerName, customerEmail, customerPhoneNumber, customerEnglishName,
            customerAddress, customerInfoAgreement, customerStatus, customerRegisteredDate, nationCodeFk,
            customerGender, nationName, customerType, membershipLevelName, orderBy, sortBy, pageNum);

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

            String fileName = "Customer.xlsx";
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
