package org.iot.hotelitybackend.hotelmanagement.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iot.hotelitybackend.hotelmanagement.aggregate.AncillaryEntity;
import org.iot.hotelitybackend.hotelmanagement.dto.AncillaryDTO;
import org.iot.hotelitybackend.hotelmanagement.repository.AncillaryCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.AncillaryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestModifyFacility;
import org.iot.hotelitybackend.hotelmanagement.vo.RequestRegistFacility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AncillaryServiceImpl implements AncillaryService{

	private final AncillaryRepository ancillaryRepository;
	private final AncillaryCategoryRepository ancillaryCategoryRepository;
	private final BranchRepository branchRepository;
	private final ModelMapper mapper;

	@Autowired
	public AncillaryServiceImpl(AncillaryRepository ancillaryRepository, AncillaryCategoryRepository ancillaryCategoryRepository, BranchRepository branchRepository, ModelMapper mapper) {
		this.ancillaryRepository = ancillaryRepository;
		this.ancillaryCategoryRepository = ancillaryCategoryRepository;
		this.branchRepository = branchRepository;
		this.mapper = mapper;
	}

	@Override
	public Map<String, Object> selectAllFacilities(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		Page<AncillaryEntity> ancillaryEntityPage = ancillaryRepository.findAll(pageable);
		List<AncillaryDTO> ancillaryDTOList = ancillaryEntityPage
			.stream()
			.map(ancillaryEntity -> mapper.map(ancillaryEntity, AncillaryDTO.class))
			.peek(ancillaryDTO -> {

				// 지점 이름 가져와 붙이기
				ancillaryDTO.setBranchName(
					branchRepository.findById(
						ancillaryDTO.getBranchCodeFk()
					).orElseThrow(IllegalArgumentException::new).getBranchName()
				);

				// 부대시설 카테고리 이름 가져와 붙이기
				ancillaryDTO.setAncillaryCategoryName(
					ancillaryCategoryRepository.findById(
						ancillaryDTO.getAncillaryCategoryCodeFk()
					).orElseThrow(IllegalArgumentException::new).getAncillaryCategoryName()
				);
			})
			.toList();

		int totalPagesCount = ancillaryEntityPage.getTotalPages();
		int currentPageIndex = ancillaryEntityPage.getNumber();

		Map<String, Object> ancillaryPageInfo = new HashMap<>();

		ancillaryPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
		ancillaryPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
		ancillaryPageInfo.put(KEY_CONTENT, ancillaryDTOList);

		return ancillaryPageInfo;
	}

	@Override
	public Map<String, Object> registFacility(RequestRegistFacility requestRegistFacility) {
		AncillaryEntity ancillaryEntity = AncillaryEntity.builder()
				.ancillaryName(requestRegistFacility.getAncillaryName())
				.branchCodeFk(requestRegistFacility.getBranchCodeFk())
				.ancillaryLocation(requestRegistFacility.getAncillaryLocation())
				.ancillaryOpenTime(requestRegistFacility.getAncillaryOpenTime())
				.ancillaryCloseTime(requestRegistFacility.getAncillaryCloseTime())
				.ancillaryPhoneNumber(requestRegistFacility.getAncillaryPhoneNumber())
				.ancillaryCategoryCodeFk(requestRegistFacility.getAncillaryCategoryCodeFk())
				.build();

		HashMap<String, Object> registFacilityInfo;
		registFacilityInfo = new HashMap<>();

		registFacilityInfo.put(KEY_CONTENT, mapper.map(ancillaryRepository.save(ancillaryEntity), AncillaryDTO.class));

		return registFacilityInfo;
	}

	@Override
	public Map<String, Object> modifyFacilityInfo(RequestModifyFacility requestModifyFacility, int ancillaryCodePk) {
		AncillaryEntity ancillaryEntity = AncillaryEntity.builder()
				.ancillaryCodePk(ancillaryCodePk)
				.ancillaryName(requestModifyFacility.getAncillaryName())
				.branchCodeFk(requestModifyFacility.getBranchCodeFk())
				.ancillaryLocation(requestModifyFacility.getAncillaryLocation())
				.ancillaryOpenTime(requestModifyFacility.getAncillaryOpenTime())
				.ancillaryCloseTime(requestModifyFacility.getAncillaryCloseTime())
				.ancillaryPhoneNumber(requestModifyFacility.getAncillaryPhoneNumber())
				.ancillaryCategoryCodeFk(requestModifyFacility.getAncillaryCategoryCodeFk())
				.build();

		Map<String, Object> modifyFacilityInfo = new HashMap<>();

		modifyFacilityInfo.put(KEY_CONTENT, mapper.map(ancillaryRepository.save(ancillaryEntity), AncillaryDTO.class));

		return modifyFacilityInfo;
	}

	@Override
	public Map<String, Object> deleteFacilityInfo(int ancillaryCodePk) {
		Map<String, Object> deleteFacilityInfo = new HashMap<>();

		if (ancillaryRepository.existsById(ancillaryCodePk)) {
			ancillaryRepository.deleteById(ancillaryCodePk);
		} else {
			System.out.println("해당하는 부대 시설을 찾을 수 없습니다.");
		}

		return deleteFacilityInfo;
	}

	@Override
	public List<AncillaryDTO> selectAllFacilitiesForExcel() {
		return ancillaryRepository.findAll()
			.stream()
			.map(ancillaryEntity -> mapper.map(ancillaryEntity, AncillaryDTO.class))
			.peek(ancillaryDTO -> {

				// 지점 이름 가져와 붙이기
				ancillaryDTO.setBranchName(
					branchRepository.findById(
						ancillaryDTO.getBranchCodeFk()
					).orElseThrow(IllegalArgumentException::new).getBranchName()
				);

				// 부대시설 카테고리 이름 가져와 붙이기
				ancillaryDTO.setAncillaryCategoryName(
					ancillaryCategoryRepository.findById(
						ancillaryDTO.getAncillaryCategoryCodeFk()
					).orElseThrow(IllegalArgumentException::new).getAncillaryCategoryName()
				);
			})
			.toList();
	}

	@Override
	public Map<String, Object> createFacilitiesExcelFile(List<AncillaryDTO> ancillaryDTOList) throws
		IOException,
		NoSuchFieldException,
		IllegalAccessException{
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Sheet sheet = workbook.createSheet("부대시설");

		createDashboardSheet(ancillaryDTOList, sheet, headerCellStyle);

		workbook.write(out);
		log.info("[ReportService:getExcel] create Excel list done. row count:[{}]", ancillaryDTOList.size());

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String time = dateFormat.format(calendar.getTime());
		String fileName = "Ancillary-facilities_"+ time +".xlsx";
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
	private void createDashboardSheet(List<AncillaryDTO> ancillaryDTOList, Sheet sheet, CellStyle headerCellStyle) throws
		NoSuchFieldException, IllegalAccessException {
		Row headerRow = sheet.createRow(0);
		AncillaryDTO ancillaryDTO = new AncillaryDTO();
		int idx1 = 0;
		Cell headerCell;
		List<String> headerStrings = new ArrayList<>();
		for (Field field : ancillaryDTO.getClass().getDeclaredFields()) {
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
		for (AncillaryDTO ancillaryDTOIter : ancillaryDTOList) {
			bodyRow = sheet.createRow(idx2++);
			int idx3 = 0;
			for (String headerString : headerStrings) {
				Field field = ancillaryDTOIter.getClass().getDeclaredField(headerString);
				field.setAccessible(true);
				bodyCell = bodyRow.createCell(idx3);
				bodyCell.setCellValue(String.valueOf(field.get(ancillaryDTOIter)));
				idx3++;
			}
		}

		for (int i = 0; i < headerStrings.size(); i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + (short)1024);
		}
	}
}
