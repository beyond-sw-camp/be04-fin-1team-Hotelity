package org.iot.hotelitybackend.customer.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;

public interface CustomerService {
    Map<String, Object> selectCustomersList(String customerType, String membershipLevelName, int pageNum);

    CustomerDTO selectCustomerByCustomerCodePk(int customerCodePk);

	Map<String, Object> readExcel(Workbook workbook);

	ByteArrayInputStream downloadExcel() throws IOException;
}
