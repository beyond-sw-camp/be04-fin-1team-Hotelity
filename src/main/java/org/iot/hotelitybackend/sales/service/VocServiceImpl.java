package org.iot.hotelitybackend.sales.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomCategoryDTO;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomDTO;
import org.iot.hotelitybackend.sales.aggregate.VocEntity;
import org.iot.hotelitybackend.sales.aggregate.VocSpecification;
import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.repository.VocRepository;
import org.iot.hotelitybackend.sales.vo.RequestReplyVoc;
import org.iot.hotelitybackend.sales.vo.ResponseVoc;
import org.iot.hotelitybackend.sales.vo.VocDashboardVO;
import org.iot.hotelitybackend.sales.vo.VocSearchCriteria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VocServiceImpl implements VocService {

	private final ModelMapper mapper;

	private final VocRepository vocRepository;

	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public VocServiceImpl(ModelMapper mapper, VocRepository vocRepository, CustomerRepository customerRepository,
		EmployeeRepository employeeRepository) {
		this.mapper = mapper;
		this.vocRepository = vocRepository;
		this.customerRepository = customerRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Map<String, Object> selectVocsList(
		// int pageNum, Integer vocCodePk, String vocTitle, String vocCategory,
		// Integer customerCodeFk, String customerName, LocalDateTime vocCreatedDate, LocalDateTime vocLastUpdatedDate,
		// String branchCodeFk,
		// Integer employeeCodeFk, String PICEmployeeName, Integer vocProcessStatus, String orderBy, Integer sortBy
		VocSearchCriteria criteria
	) {

		Integer pageNum = criteria.getPageNum();
		String orderBy = criteria.getOrderBy();
		Integer sortBy = criteria.getSortBy();

		Pageable pageable;

		if (orderBy == null) {
			pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("vocCodePk"));
		} else {
			if (sortBy == 1) {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
			} else {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
			}
		}

		Specification<VocEntity> spec = buildSpecification(criteria);
		// Specification<VocEntity> spec = (root, query, criteriaBuilder) -> null;
		//
		// // voc코드
		// if (vocCodePk != null) {
		// 	spec = spec.and(VocSpecification.equalsVocCodePk(vocCodePk));
		// }
		//
		// // voc 제목
		// if (vocTitle != null) {
		// 	spec = spec.and(VocSpecification.likeVocTitle(vocTitle));
		// }
		//
		// // voc 카테고리
		// if (vocCategory != null) {
		// 	spec = spec.and(VocSpecification.likeVocCategory(vocCategory));
		// }
		//
		// // 고객코드
		// if (customerCodeFk != null) {
		// 	spec = spec.and(VocSpecification.equalsCustomerCode(customerCodeFk));
		// }
		//
		// // 고객명
		// if (customerName != null) {
		// 	spec = spec.and(VocSpecification.likeCustomerName(customerName));
		// }
		//
		// // voc 작성일자
		// if (vocCreatedDate != null) {
		// 	spec = spec.and(VocSpecification.equalsVocCreatedDate(vocCreatedDate));
		// }
		//
		// // voc 업데이트 일자
		// if (vocLastUpdatedDate != null) {
		// 	spec = spec.and(VocSpecification.equalsVocLastUpdatedDate(vocLastUpdatedDate));
		// }
		//
		// // 지점코드
		// if (branchCodeFk != null) {
		// 	spec = spec.and(VocSpecification.equalsBranchCode(branchCodeFk));
		// }
		//
		// // 직원코드
		// if (employeeCodeFk != null) {
		// 	spec = spec.and(VocSpecification.equalsEmployeeCodeFk(employeeCodeFk));
		// }
		//
		// // 직원명
		// if (PICEmployeeName != null) {
		// 	spec = spec.and(VocSpecification.likeEmployeeName(PICEmployeeName));
		// }
		//
		// // voc 처리상태
		// if (vocProcessStatus != null) {
		// 	spec = spec.and(VocSpecification.equalsVocProcessStatus(vocProcessStatus));
		// }

		Page<VocEntity> vocEntityPage = vocRepository.findAll(spec, pageable);
		List<VocDTO> vocDTOList = vocEntityPage.stream()
			.map(vocEntity -> mapper.map(vocEntity, VocDTO.class))
			.peek(vocDTO -> vocDTO.setCustomerName(
				mapper.map(customerRepository.findById(vocDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()
			))
			.peek(vocDTO -> vocDTO.setPICEmployeeName(
				mapper.map(employeeRepository.findById(vocDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
			))
			.toList();

		int totalPagesCount = vocEntityPage.getTotalPages();
		int currentPageIndex = vocEntityPage.getNumber();

		Map<String, Object> vocPageInfo = new HashMap<>();

		vocPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		vocPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		vocPageInfo.put(KEY_CONTENT, vocDTOList);

		return vocPageInfo;
	}

	private Specification<VocEntity> buildSpecification(VocSearchCriteria criteria) {

		Integer pageNum = criteria.getPageNum();
		Integer vocCodePk = criteria.getVocCodePk();
		String vocTitle = criteria.getVocTitle();
		String vocCategory = criteria.getVocCategory();
		Integer customerCodeFk = criteria.getCustomerCodeFk();
		String customerName = criteria.getCustomerName();
		LocalDateTime vocCreatedDate = criteria.getVocCreatedDate();
		LocalDateTime vocLastUpdatedDate = criteria.getVocLastUpdatedDate();
		String branchCodeFk = criteria.getBranchCodeFk();
		Integer employeeCodeFk = criteria.getEmployeeCodeFk();
		String employeeName = criteria.getEmployeeName();
		Integer vocProcessStatus = criteria.getVocProcessStatus();

		Specification<VocEntity> spec = (root, query, criteriaBuilder) -> null;

		// voc코드
		if (vocCodePk != null) {
			spec = spec.and(VocSpecification.equalsVocCodePk(vocCodePk));
		}

		// voc 제목
		if (vocTitle != null) {
			spec = spec.and(VocSpecification.likeVocTitle(vocTitle));
		}

		// voc 카테고리
		if (vocCategory != null) {
			spec = spec.and(VocSpecification.likeVocCategory(vocCategory));
		}

		// 고객코드
		if (customerCodeFk != null) {
			spec = spec.and(VocSpecification.equalsCustomerCode(customerCodeFk));
		}

		// 고객명
		if (customerName != null) {
			spec = spec.and(VocSpecification.likeCustomerName(customerName));
		}

		// voc 작성일자
		if (vocCreatedDate != null) {
			spec = spec.and(VocSpecification.equalsVocCreatedDate(vocCreatedDate));
		}

		// voc 업데이트 일자
		if (vocLastUpdatedDate != null) {
			spec = spec.and(VocSpecification.equalsVocLastUpdatedDate(vocLastUpdatedDate));
		}

		// 지점코드
		if (branchCodeFk != null) {
			spec = spec.and(VocSpecification.equalsBranchCode(branchCodeFk));
		}

		// 직원코드
		if (employeeCodeFk != null) {
			spec = spec.and(VocSpecification.equalsEmployeeCodeFk(employeeCodeFk));
		}

		// 직원명
		if (employeeName != null) {
			spec = spec.and(VocSpecification.likeEmployeeName(employeeName));
		}

		// voc 처리상태
		if (vocProcessStatus != null) {
			spec = spec.and(VocSpecification.equalsVocProcessStatus(vocProcessStatus));
		}
		return spec;
	}

	@Override
	public VocDTO selectVocByVocCodePk(int vocCodePk) {
		VocEntity vocEntity = vocRepository.findById(vocCodePk)
			.orElseThrow(IllegalArgumentException::new);

		String customerName = customerRepository
			.findById(vocEntity.getCustomerCodeFk())
			.get()
			.getCustomerName();

		String employeeName = employeeRepository
			.findById(vocEntity.getEmployeeCodeFk())
			.get()
			.getEmployeeName();

		VocDTO vocDTO = mapper.map(vocEntity, VocDTO.class);

		vocDTO.setCustomerName(customerName);
		vocDTO.setPICEmployeeName(employeeName);

		return vocDTO;
	}

	@Override
	public Map<String, Object> replyVoc(RequestReplyVoc requestReplyVoc, int vocCodePk) {
		VocEntity vocEntity = VocEntity.builder()
			.vocCodePk(vocCodePk)
			.vocTitle(vocRepository.findById(vocCodePk).get().getVocTitle())
			.vocContent(vocRepository.findById(vocCodePk).get().getVocContent())
			.vocCreatedDate(vocRepository.findById(vocCodePk).get().getVocCreatedDate())
			.vocLastUpdatedDate(LocalDateTime.now())
			.customerCodeFk(vocRepository.findById(vocCodePk).get().getCustomerCodeFk())
			.vocCategory(vocRepository.findById(vocCodePk).get().getVocCategory())
			.employeeCodeFk(vocRepository.findById(vocCodePk).get().getEmployeeCodeFk())
			.branchCodeFk(vocRepository.findById(vocCodePk).get().getBranchCodeFk())
			.vocImageLink(requestReplyVoc.getVocImageLink())
			.vocResponse(requestReplyVoc.getVocResponse())
			.vocProcessStatus(1)
			.build();

		Map<String, Object> vocReply = new HashMap<>();

		vocReply.put(KEY_CONTENT, mapper.map(vocRepository.save(vocEntity), VocDTO.class));

		return vocReply;
	}

	@Override
	public List<VocDTO> selectVocsListForExcel() {
		return vocRepository.findAll()
			.stream()
			.map(vocEntity -> mapper.map(vocEntity, VocDTO.class))
			.peek(vocDTO -> vocDTO.setCustomerName(
				mapper.map(customerRepository.findById(vocDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()
			))
			.peek(vocDTO -> vocDTO.setPICEmployeeName(
				mapper.map(employeeRepository.findById(vocDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
			))
			.toList();
	}

	@Override
	public Map<String, Object> deleteVoc(int vocCodePk) {
		Map<String, Object> deleteVoc = new HashMap<>();
		try {
			vocRepository.deleteById(vocCodePk);
			deleteVoc.put(KEY_CONTENT, "Content deleted successfully.");
		} catch (Exception e) {
			deleteVoc.put(KEY_CONTENT, "Failed to delete content.");
		}
		return deleteVoc;
	}

	@Override
	public Map<String, Object> selectLatestVocList() {
		List<VocEntity> vocEntityList = vocRepository.findTop3ByOrderByVocCodePkDesc();
		List<VocDashboardVO> vocDashboardVOList = vocEntityList
			.stream()
			.map(vocEntity -> mapper.map(vocEntity, VocDashboardVO.class))
			.peek(vocDashboardVO -> vocDashboardVO.setPICEmployeeName(
				employeeRepository.findById(
					vocDashboardVO.getEmployeeCodeFk()
				).get().getEmployeeName()
			))
			.collect(Collectors.toList());

		// VOC 개수가 3개보다 부족할 때
		if (vocDashboardVOList.size() < 3) {
			int limit = 3 - vocDashboardVOList.size();
			for (int i = 0; i < limit; i++) {
				VocDashboardVO vocDashboardVO = new VocDashboardVO();
				vocDashboardVO.setVocTitle("VOC가 없습니다");
				vocDashboardVOList.add(vocDashboardVO);
			}
		}

		Map<String, Object> latestVocList = new HashMap<>();
		latestVocList.put(KEY_CONTENT, vocDashboardVOList);

		return latestVocList;
	}

	@Override
	public Map<String, Object> createVocsExcelFile(List<VocDTO> vocDTOList) throws
		IOException,
		NoSuchFieldException,
		IllegalAccessException {

		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Sheet sheet = workbook.createSheet("VOC");

		createDashboardSheet(vocDTOList, sheet, headerCellStyle);

		workbook.write(out);
		log.info("[ReportService:getExcel] create Excel list done. row count:[{}]", vocDTOList.size());

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String time = dateFormat.format(calendar.getTime());
		String fileName = "VOCs_" + time + ".xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/vnd.ms-excel");
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		Map<String, Object> result = new HashMap<>();
		result.put("result", new ByteArrayInputStream(out.toByteArray()));
		result.put("fileName", fileName);
		result.put("headers", headers);
		return result;
	}

	// 첫번째 인자인 List<RoomDTO> 만 바꿔서 쓰면 됨
	private void createDashboardSheet(List<VocDTO> vocDTOList, Sheet sheet, CellStyle headerCellStyle) throws
		NoSuchFieldException, IllegalAccessException {
		Row headerRow = sheet.createRow(0);
		VocDTO vocDTO = new VocDTO();
		int idx1 = 0;
		Cell headerCell;
		List<String> headerStrings = new ArrayList<>();
		for (Field field : vocDTO.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			headerStrings.add(fieldName);
			headerCell = headerRow.createCell(idx1++);
			headerCell.setCellValue(fieldName);
			headerCell.setCellStyle(headerCellStyle);
		}

		Row bodyRow;
		Cell bodyCell;
		int idx2 = 1;
		for (VocDTO vocDTOIter : vocDTOList) {
			bodyRow = sheet.createRow(idx2++);
			int idx3 = 0;
			for (String headerString : headerStrings) {
				Field field = vocDTOIter.getClass().getDeclaredField(headerString);
				field.setAccessible(true);
				bodyCell = bodyRow.createCell(idx3);
				bodyCell.setCellValue(String.valueOf(field.get(vocDTOIter)));
				idx3++;
			}
		}

		for (int i = 0; i < headerStrings.size(); i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + (short)1024);
		}
	}
}
