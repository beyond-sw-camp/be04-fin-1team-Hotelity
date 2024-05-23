package org.iot.hotelitybackend.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
import org.springframework.http.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtil {

	public static Map<String, Object> createExcelFile(List<?> dtoList, String title, String[] headerStrings) throws
		IOException,
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

		Sheet sheet = workbook.createSheet(title);

		// 엑셀 시트 만들기
		createDashboardSheet(dtoList, sheet, headerCellStyle, headerStrings);
		workbook.write(out);
		log.info("[ReportService:getExcel] create Excel list done. row count:[{}]", dtoList.size());

		// 파일명에 현재시간 넣기 위한 작업
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String time = dateFormat.format(calendar.getTime());

		// UTF8 로 인코딩 해줘야 파일명에 한글 들어갔을 때 오류 발생 안함
		String fileName = URLEncoder.encode(title + "_" + time + ".xlsx", "UTF-8");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/vnd.ms-excel");
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		Map<String, Object> result = new HashMap<>();
		result.put("result", new ByteArrayInputStream(out.toByteArray()));
		result.put("fileName", fileName);
		result.put("headers", headers);
		return result;
	}

	private static void createDashboardSheet(
		List<?> dtoList,
		Sheet sheet,
		CellStyle headerCellStyle,
		String[] headerStrings
	) throws IllegalAccessException {

		Row headerRow = sheet.createRow(0);
		Cell headerCell;

		Row bodyRow;
		Cell bodyCell;

		// header (컬럼명 들어갈 맨 첫번째 줄)
		for (int i = 0; i < headerStrings.length; i++) {
			headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headerStrings[i]);
			headerCell.setCellStyle(headerCellStyle);
		}

		//
		int j = 1;
		for (Object dto : dtoList) {
			Field[] fields = dto.getClass().getDeclaredFields();
			bodyRow = sheet.createRow(j++);
			for (int k = 0; k < fields.length; k++) {
				fields[k].setAccessible(true);
				bodyCell = bodyRow.createCell(k);
				bodyCell.setCellValue(String.valueOf(fields[k].get(dto)));

				// 컬럼 너비 조정
				if (j == dtoList.size()) {
					sheet.autoSizeColumn(k);
					sheet.setColumnWidth(k, sheet.getColumnWidth(k) + (short)1024);
				}
			}
		}
	}
}
