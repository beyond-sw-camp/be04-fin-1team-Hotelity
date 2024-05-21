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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public VocServiceImpl(ModelMapper mapper, VocRepository vocRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.mapper = mapper;
        this.vocRepository = vocRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Map<String, Object> selectVocsList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<VocEntity> vocPage = vocRepository.findAll(pageable);
        List<VocDTO> vocDTOList = vocPage.stream().map(vocEntity -> mapper.map(vocEntity, VocDTO.class))
                .peek(vocDTO -> vocDTO.setCustomerName(
                        mapper.map(customerRepository.findById(vocDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()
                ))
                .peek(vocDTO -> vocDTO.setEmployeeName(
                        mapper.map(employeeRepository.findById(vocDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
                ))
                .toList();

        int totalPagesCount = vocPage.getTotalPages();
        int currentPageIndex = vocPage.getNumber();

        Map<String, Object> vocPageInfo = new HashMap<>();

        vocPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        vocPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        vocPageInfo.put(KEY_CONTENT, vocDTOList);

        return vocPageInfo;
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
        vocDTO.setEmployeeName(employeeName);

        return vocDTO;
    }

    @Override
    public Map<String, Object> replyVoc(RequestReplyVoc requestReplyVoc, int vocCodePk) {
        VocEntity vocEntity = VocEntity.builder()
                .vocCodePk(vocCodePk)
                .vocTitle(vocRepository.findById(vocCodePk).get().getVocTitle())
                .vocContent(vocRepository.findById(vocCodePk).get().getVocContent())
                .vocCreatedDate(vocRepository.findById(vocCodePk).get().getVocCreatedDate())
                .vocLastUpdatedDate(new Date())
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
    public Map<String, Object> selectSearchedVocsList(int pageNum, String branchCodeFk, Integer vocProcessStatus, String vocCategory, Date vocCreatedDate, Integer customerCodeFk) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Specification<VocEntity> spec = (root, query, criteriaBuilder) -> null;

        if (!branchCodeFk.isEmpty()) {
            spec = spec.and(VocSpecification.equalsBranchCode(branchCodeFk));
        }

        if (vocProcessStatus != null) {
            spec = spec.and(VocSpecification.equalsVocProcessStatus(vocProcessStatus));
        }

        if (!vocCategory.isEmpty()) {
            spec = spec.and(VocSpecification.equalsVocCategory(vocCategory));
        }

        if (vocCreatedDate != null) {
            spec = spec.and(VocSpecification.equalsVocCreatedDate(vocCreatedDate));
        }

        if (customerCodeFk != null) {
            spec = spec.and(VocSpecification.equalsCustomerCode(customerCodeFk));
        }

        Page<VocEntity> vocEntityPage = vocRepository.findAll(spec, pageable);
        List<VocDTO> vocDTOList = vocEntityPage
                .stream()
                .map(vocEntity -> mapper.map(vocEntity, VocDTO.class))
                .peek(vocDTO -> vocDTO.setCustomerName(
                        mapper.map(customerRepository.findById(vocDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()
                ))
                .peek(vocDTO -> vocDTO.setEmployeeName(
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

    @Override
    public List<VocDTO> selectVocsListForExcel() {
        return vocRepository.findAll()
            .stream()
            .map(vocEntity -> mapper.map(vocEntity, VocDTO.class))
            .peek(vocDTO -> vocDTO.setCustomerName(
                mapper.map(customerRepository.findById(vocDTO.getCustomerCodeFk()), CustomerDTO.class).getCustomerName()
            ))
            .peek(vocDTO -> vocDTO.setEmployeeName(
                mapper.map(employeeRepository.findById(vocDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
            ))
            .toList();
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
        String fileName = "VOCs_"+ time +".xlsx";
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
