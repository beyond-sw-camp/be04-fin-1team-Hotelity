package org.iot.hotelitybackend.customer.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.service.CustomerService;
import org.iot.hotelitybackend.customer.vo.ResponseCustomer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{pageNum}")
    public ResponseEntity<ResponseVO> selectCustomersList(@PathVariable("pageNum") int pageNum) {
        Map<String, Object> customerPageInfo = customerService.selectCustomersList(pageNum);

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
}
