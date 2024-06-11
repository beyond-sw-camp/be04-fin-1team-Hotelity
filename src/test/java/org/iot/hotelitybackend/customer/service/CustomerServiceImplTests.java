package org.iot.hotelitybackend.customer.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.common.vo.CustomerCriteria;
import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.dto.SelectCustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelservice.dto.PaymentDTO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.hotelservice.service.PaymentServiceImpl;
import org.iot.hotelitybackend.hotelservice.service.StayServiceImpl;
import org.iot.hotelitybackend.hotelservice.vo.PaymentSearchCriteria;
import org.iot.hotelitybackend.hotelservice.vo.StaySearchCriteria;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.sales.dto.CouponIssueDTO;
import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.iot.hotelitybackend.sales.service.CouponIssueServiceImpl;
import org.iot.hotelitybackend.sales.service.VocServiceImpl;
import org.iot.hotelitybackend.customer.repository.NationRepository;
import org.iot.hotelitybackend.sales.vo.CouponIssueSearchCriteria;
import org.iot.hotelitybackend.sales.vo.VocSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.iot.hotelitybackend.common.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerServiceImplTests {

	@Mock
	private ModelMapper mapper;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private NationRepository nationRepository;

	@Mock
	private MembershipRepository membershipRepository;

	@Mock
	private MembershipIssueRepository membershipIssueRepository;

	@Mock
	private PaymentServiceImpl paymentService;

	@Mock
	private VocServiceImpl vocService;

	@Mock
	private StayServiceImpl stayService;

	@Mock
	private CouponIssueServiceImpl couponIssueService;

	@InjectMocks
	private CustomerServiceImpl customerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSelectCustomersList() {
		CustomerCriteria criteria = new CustomerCriteria();
		criteria.setPageNum(0);
		criteria.setOrderBy("customerCodePk");
		criteria.setSortBy(1);

		CustomerEntity customerEntity = new CustomerEntity();
		Page<CustomerEntity> customerPage = new PageImpl<>(Arrays.asList(customerEntity));
		when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);
		when(mapper.map(any(CustomerEntity.class), eq(CustomerDTO.class))).thenReturn(new CustomerDTO());

		Map<String, Object> result = customerService.selectCustomersList(criteria);

		assertNotNull(result);
		verify(customerRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void testSelectCustomerByCustomerCodePk() {
		Integer customerCodePk = 1;
		CustomerEntity customerEntity = new CustomerEntity();
		when(customerRepository.findById(customerCodePk)).thenReturn(Optional.of(customerEntity));
		when(mapper.map(any(CustomerEntity.class), eq(SelectCustomerDTO.class))).thenReturn(new SelectCustomerDTO());

		Map<String, Object> paymentMap = new HashMap<>();
		paymentMap.put(KEY_CONTENT, Arrays.asList(new PaymentDTO()));
		when(paymentService.selectPaymentLogList(any(PaymentSearchCriteria.class))).thenReturn(paymentMap);

		Map<String, Object> vocMap = new HashMap<>();
		vocMap.put(KEY_CONTENT, Arrays.asList(new VocDTO()));
		when(vocService.selectVocsList(any(VocSearchCriteria.class))).thenReturn(vocMap);

		Map<String, Object> stayMap = new HashMap<>();
		stayMap.put(KEY_CONTENT, Arrays.asList(new StayDTO()));
		when(stayService.selectStaysList(any(StaySearchCriteria.class))).thenReturn(stayMap);

		Map<String, Object> couponIssueMap = new HashMap<>();
		couponIssueMap.put(KEY_CONTENT, Arrays.asList(new CouponIssueDTO()));
		when(couponIssueService.selectCouponIssueList(any(CouponIssueSearchCriteria.class))).thenReturn(couponIssueMap);

		SelectCustomerDTO result = customerService.selectCustomerByCustomerCodePk(customerCodePk);

		assertNotNull(result);
		verify(customerRepository, times(1)).findById(customerCodePk);
	}

	@Test
	void testReadExcel() throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("Email");
		row.createCell(2).setCellValue("Phone");
		row.createCell(3).setCellValue("English Name");
		row.createCell(4).setCellValue("Address");
		row.createCell(5).setCellValue(1);
		row.createCell(6).setCellValue(1);
		row.createCell(7).setCellValue(new Date());
		row.createCell(8).setCellValue("Type");
		row.createCell(9).setCellValue(1);
		row.createCell(10).setCellValue("Gender");

		row = sheet.createRow(1);
		row.createCell(0).setCellValue("John Doe");
		row.createCell(1).setCellValue("john.doe@example.com");
		row.createCell(2).setCellValue("1234567890");
		row.createCell(3).setCellValue("John");
		row.createCell(4).setCellValue("123 Street");
		row.createCell(5).setCellValue(1);
		row.createCell(6).setCellValue(1);
		row.createCell(7).setCellValue(new Date());
		row.createCell(8).setCellValue("VIP");
		row.createCell(9).setCellValue(1);
		row.createCell(10).setCellValue("Male");

		when(customerRepository.save(any(CustomerEntity.class))).thenReturn(new CustomerEntity());
		when(membershipIssueRepository.save(any(MembershipIssueEntity.class))).thenReturn(new MembershipIssueEntity());

		Map<String, Object> result = customerService.readExcel(workbook);

		assertNotNull(result);
		assertEquals("success", result.get(KEY_CONTENT));
	}

	@Test
	void testDeleteCustomerByCustomerCodePk() {
		Integer customerCodePk = 1;
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setCustomerCodePk(customerCodePk);
		customerEntity.setCustomerName("John Doe");
		customerEntity.setCustomerEmail("john.doe@example.com");
		customerEntity.setCustomerPhoneNumber("1234567890");
		customerEntity.setCustomerEnglishName("John");
		customerEntity.setCustomerAddress("123 Street");
		customerEntity.setCustomerInfoAgreement(1);
		customerEntity.setCustomerStatus(1);
		customerEntity.setCustomerRegisteredDate(new Date());
		customerEntity.setCustomerType("VIP");
		customerEntity.setNationCodeFk(1);
		customerEntity.setCustomerGender("Male");

		when(customerRepository.findById(customerCodePk)).thenReturn(Optional.of(customerEntity));
		when(customerRepository.save(any(CustomerEntity.class))).thenReturn(new CustomerEntity());

		Map<String, Object> result = customerService.deleteCustomerByCustomerCodePk(customerCodePk);

		assertNotNull(result);
		assertEquals("success", result.get(KEY_CONTENT));
	}

	@Test
	void testInsertCustomer() {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerName("John Doe");
		customerDTO.setCustomerEmail("john.doe@example.com");
		customerDTO.setCustomerPhoneNumber("1234567890");
		customerDTO.setCustomerEnglishName("John");
		customerDTO.setCustomerAddress("123 Street");
		customerDTO.setCustomerInfoAgreement(1);
		customerDTO.setCustomerStatus(1);
		customerDTO.setCustomerType("VIP");
		customerDTO.setNationCodeFk(1);
		customerDTO.setCustomerGender("Male");

		// Mocking the saved entity
		CustomerEntity customerEntity = CustomerEntity.builder()
			.customerCodePk(1)  // Assuming the ID is 1
			.customerName(customerDTO.getCustomerName())
			.customerEmail(customerDTO.getCustomerEmail())
			.customerPhoneNumber(customerDTO.getCustomerPhoneNumber())
			.customerEnglishName(customerDTO.getCustomerEnglishName())
			.customerAddress(customerDTO.getCustomerAddress())
			.customerInfoAgreement(customerDTO.getCustomerInfoAgreement())
			.customerStatus(customerDTO.getCustomerStatus())
			.customerRegisteredDate(new Date())
			.customerType(customerDTO.getCustomerType())
			.nationCodeFk(customerDTO.getNationCodeFk())
			.customerGender(customerDTO.getCustomerGender())
			.build();

		// Mocking save and findById methods
		when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
		when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customerEntity));
		when(membershipIssueRepository.save(any(MembershipIssueEntity.class))).thenReturn(new MembershipIssueEntity());

		Map<String, Object> result = customerService.insertCustomer(customerDTO);

		// Assertions
		assertNotNull(result);
		assertEquals("John Doe님의 정보가 성공적으로 등록되었습니다.", result.get("content"));

		// Verify that save method was called
		verify(customerRepository, times(1)).save(any(CustomerEntity.class));
		verify(membershipIssueRepository, times(1)).save(any(MembershipIssueEntity.class));
		verify(customerRepository, times(1)).findById(anyInt());
	}


	@Test
	void testDownloadExcel() throws IOException {
		List<CustomerEntity> customerEntities = Arrays.asList(new CustomerEntity());
		when(customerRepository.findAll(any(Specification.class))).thenReturn(customerEntities);
		when(mapper.map(any(CustomerEntity.class), eq(CustomerDTO.class))).thenReturn(new CustomerDTO());

		ByteArrayInputStream result = customerService.downloadExcel(
			1, "John Doe", "john.doe@example.com", "1234567890", "John",
			"123 Street", 1, 1, new Date(), 1, "Male",
			"USA", "VIP", "Gold"
		);

		assertNotNull(result);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		result.transferTo(baos);
		byte[] bytes = baos.toByteArray();
		assertTrue(bytes.length > 0);
	}
}
