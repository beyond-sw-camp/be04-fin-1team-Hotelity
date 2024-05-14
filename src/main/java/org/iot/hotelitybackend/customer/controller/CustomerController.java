package org.iot.hotelitybackend.customer.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.service.CustomerService;
import org.iot.hotelitybackend.customer.vo.ResponseCustomer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ResponseVO> selectCustomersList(
        @RequestParam(required = false) String customerType,
        @RequestParam(required = false) String membershipLevelName,
        @RequestParam int pageNum) {
        Map<String, Object> customerPageInfo = customerService.selectCustomersList(customerType, membershipLevelName, pageNum);

        ResponseVO response = ResponseVO.builder()
                .data(customerPageInfo)
                .resultCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }

    @GetMapping("/{customerCodePk}/customer")
    public ResponseEntity<ResponseCustomer> selectCustomerByCustomerCodePk(@PathVariable("customerCodePk") int customerCodePk){

        CustomerDTO customer = customerService.selectCustomerByCustomerCodePk(customerCodePk);
        ResponseCustomer responseCustomer = mapper.map(customer, ResponseCustomer.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseCustomer);
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
}
