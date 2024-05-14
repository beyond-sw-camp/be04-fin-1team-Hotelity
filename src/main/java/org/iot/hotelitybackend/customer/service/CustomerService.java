package org.iot.hotelitybackend.customer.service;

import java.util.Map;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;

public interface CustomerService {
    Map<String, Object> selectCustomersList(String customerType, String membershipLevelName, int pageNum);

    CustomerDTO selectCustomerByCustomerCodePk(int customerCodePk);
}
