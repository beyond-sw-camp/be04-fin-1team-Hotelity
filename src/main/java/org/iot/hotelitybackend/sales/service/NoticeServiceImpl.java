package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.sales.aggregate.NoticeEntity;
import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.repository.NoticeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final ModelMapper mapper;
    private final NoticeRepository noticeRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public NoticeServiceImpl(ModelMapper mapper, NoticeRepository noticeRepository, EmployeeRepository employeeRepository) {
        this.mapper = mapper;
        this.noticeRepository = noticeRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Map<String, Object> selectNoticesList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<NoticeEntity> noticePage = noticeRepository.findAll(pageable);
        List<NoticeDTO> noticeDTOList = noticePage.stream().map(noticeEntity -> mapper.map(noticeEntity, NoticeDTO.class))
                .peek(vocDTO -> vocDTO.setEmployeeName(
                        mapper.map(employeeRepository.findById(vocDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
                ))
                .toList();

        int totalPagesCount = noticePage.getTotalPages();
        int currentPageIndex = noticePage.getNumber();

        Map<String, Object> noticePageInfo = new HashMap<>();

        noticePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        noticePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        noticePageInfo.put(KEY_CONTENT, noticeDTOList);

        return noticePageInfo;
    }

    @Override
    public NoticeDTO selectNoticeByNoticeCodePk(int noticeCodePk) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeCodePk)
                .orElseThrow(IllegalArgumentException::new);

        String employeeName = employeeRepository
                .findById(noticeEntity.getEmployeeCodeFk())
                .get()
                .getEmployeeName();

        NoticeDTO noticeDTO = mapper.map(noticeEntity, NoticeDTO.class);

        noticeDTO.setEmployeeName(employeeName);

        return noticeDTO;
    }
}
