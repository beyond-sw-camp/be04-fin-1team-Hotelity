package org.iot.hotelitybackend.customer.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.iot.hotelitybackend.common.vo.CustomerCriteria;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.dto.SelectCustomerDTO;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyCustomer;

public interface CustomerService {

    SelectCustomerDTO selectCustomerByCustomerCodePk(Integer customerCodePk);

	Map<String, Object> readExcel(Workbook workbook);

	ByteArrayInputStream downloadExcel(Integer customerCodePk, String customerName, String customerEmail,
		String customerPhoneNumber, String customerEnglishName, String customerAddress, Integer customerInfoAgreement,
		Integer customerStatus, Date customerRegisteredDate, Integer nationCodeFk, String customerGender,
		String nationName, String customerType, String membershipLevelName) throws IOException;

	Map<String, Object> deleteCustomerByCustomerCodePk(int customerCodePk);

	Map<String, Object> insertCustomer(CustomerDTO customerDTO);

	Map<String, Object> selectCustomersList(CustomerCriteria criteria);

	CustomerDTO modifyCustomerByCustomerCodePk(int customerCodePk, RequestModifyCustomer requestModifyCustomer);
}
