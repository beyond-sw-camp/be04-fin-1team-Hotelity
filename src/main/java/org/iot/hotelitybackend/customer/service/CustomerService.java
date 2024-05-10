package org.iot.hotelitybackend.customer.service;

import java.util.Map;

public interface CustomerService {
    Map<String, Object> selectCustomersList(int pageNum);
}
