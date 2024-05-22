package org.iot.hotelitybackend.customer.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;

public interface CustomerService {
    Map<String, Object> selectCustomersList(Integer customerCodePk, String customerName, String customerEmail,
		String customerPhoneNumber, String customerEnglishName, String customerAddress, Integer customerInfoAgreement, Integer customerStatus,
		Date customerRegisteredDate, Integer nationCodeFk, String customerGender, String nationName, String customerType,
		String membershipLevelName, String orderBy, Integer sortBy, Integer pageNum);

    CustomerDTO selectCustomerByCustomerCodePk(int customerCodePk);

	Map<String, Object> readExcel(Workbook workbook);

	ByteArrayInputStream downloadExcel() throws IOException;
}
