package org.iot.hotelitybackend.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// @WebMvcTest(CustomerServiceImpl.class)
// @ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceImplTests {

	@Autowired
	private CustomerServiceImpl customerService;

	@Test
	void testGetCustomers() {
		CustomerDTO customer = new CustomerDTO();
		customer.setCustomerCodePk(25);
		customer.setCustomerName("소우주");
		customer.setCustomerEmail("hong7@example.com");
		customer.setCustomerPhoneNumber("01012345677");
		customer.setCustomerEnglishName("Wooju So");
		customer.setCustomerAddress("서울특별시 강남구");
		customer.setCustomerInfoAgreement(1);
		customer.setCustomerStatus(1);
		customer.setCustomerType("개인");
		customer.setNationCodeFk(12);
		customer.setCustomerGender("남");
		customer.setNationName("영국");
		customer.setMembershipLevelName("골드");

		Map<String, Object> customers = customerService.selectCustomersList(0, "개인", "골드",
			null, null, null, 0, 0, null,
			0, null, null, null, null, null, 1, 0);
		List<CustomerDTO> customerDTOList = (List<CustomerDTO>)customers.get(KEY_CONTENT);

		// System.out.println(customerDTOList);
		for (int i = 0; i < customerDTOList.size(); i++) {
			if(customerDTOList.get(i).getCustomerCodePk() == 25){
				assertThat(customerDTOList.get(i).getCustomerCodePk()).isEqualTo(customer.getCustomerCodePk());
				assertThat(customerDTOList.get(i).getCustomerName()).isEqualTo(customer.getCustomerName());
				assertThat(customerDTOList.get(i).getCustomerEmail()).isEqualTo(customer.getCustomerEmail());
				assertThat(customerDTOList.get(i).getCustomerPhoneNumber()).isEqualTo(customer.getCustomerPhoneNumber());
				assertThat(customerDTOList.get(i).getCustomerEnglishName()).isEqualTo(customer.getCustomerEnglishName());
				assertThat(customerDTOList.get(i).getCustomerAddress()).isEqualTo(customer.getCustomerAddress());
				assertThat(customerDTOList.get(i).getCustomerInfoAgreement()).isEqualTo(customer.getCustomerInfoAgreement());
				assertThat(customerDTOList.get(i).getCustomerStatus()).isEqualTo(customer.getCustomerStatus());
				assertThat(customerDTOList.get(i).getCustomerType()).isEqualTo(customer.getCustomerType());
				assertThat(customerDTOList.get(i).getNationCodeFk()).isEqualTo(customer.getNationCodeFk());
				assertThat(customerDTOList.get(i).getCustomerGender()).isEqualTo(customer.getCustomerGender());
				assertThat(customerDTOList.get(i).getNationName()).isEqualTo(customer.getNationName());
				assertThat(customerDTOList.get(i).getMembershipLevelName()).isEqualTo(customer.getMembershipLevelName());
			}
		}
	}

	@Test
	void selectCustomerByCustomerCodePk() {

	}

}