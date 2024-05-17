package org.iot.hotelitybackend.customer.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.aggregate.CustomerSpecification;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.customer.repository.NationRepository;
import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.customer.aggregate.NationEntity;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.*;
import static org.iot.hotelitybackend.common.constant.Constant.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
	private final ModelMapper mapper;
	private final CustomerRepository customerRepository;
	private final NationRepository nationRepository;
	private final MembershipRepository membershipRepository;
	private final MembershipIssueRepository membershipIssueRepository;

	@Autowired
	public CustomerServiceImpl(ModelMapper mapper, CustomerRepository customerRepository,
		NationRepository nationRepository,
		MembershipRepository membershipRepository, MembershipIssueRepository membershipIssueRepository) {
		this.mapper = mapper;
		this.customerRepository = customerRepository;
		this.nationRepository = nationRepository;
		this.membershipRepository = membershipRepository;
		this.membershipIssueRepository = membershipIssueRepository;
	}

	@Override
	public Map<String, Object> selectCustomersList(String customerType, String membershipLevelName, int page) {
		int fixedSize = 10;
		Pageable pageable = PageRequest.of(page, fixedSize, Sort.by("customerRegisteredDate").descending());

		Specification<CustomerEntity> spec = Specification.where(null);

		// 멤버십 레벨 이름으로 필터링
		if (!membershipLevelName.isEmpty()) {
			MembershipEntity membership = membershipRepository.findByMembershipLevelName(membershipLevelName);
			if (membership != null) {
				spec = spec.and(CustomerSpecification.equalsMembershipLevelName(membershipLevelName));
			}
		}

		// 고객 유형으로 필터링
		if (!customerType.isEmpty()) {
			spec = spec.and(CustomerSpecification.equalsCustomerType(customerType));
		}

		// 필터 조건에 따라 고객 정보 조회
		Page<CustomerEntity> customerPage = customerRepository.findAll(spec, pageable);
		List<CustomerDTO> customerDTOList = customerPage.stream()
			.map(customerEntity -> mapper.map(customerEntity, CustomerDTO.class))
			.peek(customerDTO -> {
				customerDTO.setNationName(nationRepository.findById(customerDTO.getNationCodeFk())
					.map(NationEntity::getNationName)
					.orElse(null));
				MembershipIssueEntity issue = membershipIssueRepository.findByCustomerCodeFk(
					customerDTO.getCustomerCodePk());
				customerDTO.setMembershipLevelName(membershipRepository.findById(issue.getMembershipLevelCodeFk())
					.map(MembershipEntity::getMembershipLevelName)
					.orElse(null));
			})
			.collect(Collectors.toList());

		Map<String, Object> customerPageInfo = new HashMap<>();
		customerPageInfo.put(KEY_TOTAL_PAGES_COUNT, customerPage.getTotalPages());
		customerPageInfo.put(KEY_CURRENT_PAGE_INDEX, customerPage.getNumber());
		customerPageInfo.put(KEY_CONTENT, customerDTOList);

		return customerPageInfo;
	}

	@Override
	public CustomerDTO selectCustomerByCustomerCodePk(int customerCodePk) {

		CustomerEntity customerEntity = customerRepository.findById(customerCodePk).get();
		return mapper.map(customerEntity, CustomerDTO.class);
	}

	@Override
	public Map<String, Object> readExcel(Workbook workbook) {
		Sheet worksheet = workbook.getSheetAt(0);

		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			Row row = worksheet.getRow(i);
			CustomerEntity customerEntity = CustomerEntity.builder()
				.customerName(row.getCell(0).getStringCellValue())
				.customerEmail(row.getCell(1).getStringCellValue())
				.customerPhoneNumber(row.getCell(2).getStringCellValue())
				.customerEnglishName(row.getCell(3).getStringCellValue())
				.customerAddress(row.getCell(4).getStringCellValue())
				.customerInfoAgreement((int)row.getCell(5).getNumericCellValue())
				.customerStatus((int)row.getCell(6).getNumericCellValue())
				.customerRegisteredDate(new Date())
				.customerType(row.getCell(8).getStringCellValue())
				.nationCodeFk((int)row.getCell(9).getNumericCellValue())
				.customerGender(row.getCell(10).getStringCellValue())
				.build();
			customerRepository.save(customerEntity);
		}

		Map<String, Object> modifiedCustomerInfo = new HashMap<>();
		modifiedCustomerInfo.put(KEY_CONTENT, "success");
		return modifiedCustomerInfo;
	}

	@Override
	public ByteArrayInputStream downloadExcel() throws IOException {
		List<CustomerEntity> customer = customerRepository.findAll();

		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Sheet customerSheet = workbook.createSheet("고객");

		createDashboardSheet(customer, customerSheet, headerCellStyle);

		workbook.write(out);
		log.info("[ReportService:getCustomerToExcel] create Customer list done. row count:[{}]", customer.size());

		return new ByteArrayInputStream(out.toByteArray());
	}

	private void createDashboardSheet(List<CustomerEntity> customer, Sheet customerSheet, CellStyle headerCellStyle) {
		Row headerRow = customerSheet.createRow(0);
		String[] headerStrings = {"고객번호", "고객이름",	"고객이메일",	"고객전화번호"	, "고객영문이름",	"고객주소", "고객개인정보제공동의여부"
			, "고객상태", "고객가입일자", "고객타입", "국가코드", "고객성별"};

		int idx = 0;
		Cell headerCell = null;
		for(String s: headerStrings){
			headerCell = headerRow.createCell(idx++);
			headerCell.setCellValue(s);
			headerCell.setCellStyle(headerCellStyle);
		}

		Row bodyRow = null;
		Cell bodyCell = null;
		int index = 1;
		for(CustomerEntity data: customer){
			bodyRow = customerSheet.createRow(index++);
			bodyCell = bodyRow.createCell(0);
			bodyCell.setCellValue(data.getCustomerCodePk());
			bodyCell = bodyRow.createCell(1);
			bodyCell.setCellValue(data.getCustomerName());
			bodyCell = bodyRow.createCell(2);
			bodyCell.setCellValue(data.getCustomerEmail());
			bodyCell = bodyRow.createCell(3);
			bodyCell.setCellValue(data.getCustomerPhoneNumber());
			bodyCell = bodyRow.createCell(4);
			bodyCell.setCellValue(data.getCustomerEnglishName());
			bodyCell = bodyRow.createCell(5);
			bodyCell.setCellValue(data.getCustomerAddress());
			bodyCell = bodyRow.createCell(6);
			bodyCell.setCellValue(data.getCustomerInfoAgreement());
			bodyCell = bodyRow.createCell(7);
			bodyCell.setCellValue(data.getCustomerStatus());
			bodyCell = bodyRow.createCell(8);
			bodyCell.setCellValue(data.getCustomerRegisteredDate());
			bodyCell = bodyRow.createCell(9);
			bodyCell.setCellValue(data.getCustomerType());
			bodyCell = bodyRow.createCell(10);
			bodyCell.setCellValue(data.getNationCodeFk());
			bodyCell = bodyRow.createCell(11);
			bodyCell.setCellValue(data.getCustomerGender());

		}

		for (int i = 0; i < headerStrings.length; i++) {
			customerSheet.autoSizeColumn(i);
			customerSheet.setColumnWidth(i, (customerSheet.getColumnWidth(i)) + (short)1024);
		}

	}
}