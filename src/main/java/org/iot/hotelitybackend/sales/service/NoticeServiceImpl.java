package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.sales.aggregate.NoticeEntity;
import org.iot.hotelitybackend.sales.aggregate.NoticeSpecification;
import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.repository.NoticeRepository;
import org.iot.hotelitybackend.sales.vo.RequestModifyNotice;
import org.iot.hotelitybackend.sales.vo.RequestNotice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
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
                .peek(noticeDTO -> noticeDTO.setEmployeeName(
                        mapper.map(employeeRepository.findById(noticeDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
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

    @Override
    public Map<String, Object> registNotice(RequestNotice requestNotice) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .noticeTitle(requestNotice.getNoticeTitle())
                .noticeContent(requestNotice.getNoticeContent())
                .employeeCodeFk(requestNotice.getEmployeeCodeFk())
                .noticePostedDate(new Date())
                .build();

        Map<String, Object> registNoticeInfo = new HashMap<>();

        registNoticeInfo.put(KEY_CONTENT, mapper.map(noticeRepository.save(noticeEntity), NoticeDTO.class));

        return registNoticeInfo;
    }

    @Override
    public Map<String, Object> modifyNotice(RequestModifyNotice requestModifyNotice, int noticeCodePk) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .noticeCodePk(noticeCodePk)
                .noticeTitle(requestModifyNotice.getNoticeTitle())
                .noticeContent(requestModifyNotice.getNoticeContent())
                .employeeCodeFk(noticeRepository.findById(noticeCodePk).get().getEmployeeCodeFk())
                .noticePostedDate(noticeRepository.findById(noticeCodePk).get().getNoticePostedDate())
                .noticeLastUpdatedDate(new Date())
                .build();

        Map<String, Object> modifyNoticeInfo = new HashMap<>();

        modifyNoticeInfo.put(KEY_CONTENT, mapper.map(noticeRepository.save(noticeEntity), NoticeDTO.class));

        return modifyNoticeInfo;
    }

    @Override
    public Map<String, Object> deleteNotice(int noticeCodePk) {
        Map<String, Object> deleteNoticeInfo = new HashMap<>();

        if (noticeRepository.existsById(noticeCodePk)) {
            noticeRepository.deleteById(noticeCodePk);
        } else {
            System.out.println("해당하는 공지를 찾을 수 없습니다.");
        }

        return deleteNoticeInfo;
    }

    @Override
    public Map<String, Object> selectSearchedNoticesList(int pageNum, String branchCodeFk) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Specification<NoticeEntity> spec = (root, query, criteriaBuilder) -> null;

        if (!branchCodeFk.isEmpty()) {
            spec = spec.and(NoticeSpecification.equalsBranchCode(branchCodeFk));
        }

        Page<NoticeEntity> noticeEntityPage = noticeRepository.findAll(spec, pageable);

        List<NoticeDTO> noticeDTOList = noticeEntityPage
                .stream()
                .map(noticeEntity -> mapper.map(noticeEntity, NoticeDTO.class))
                .toList();

        int totalPagesCount = noticeEntityPage.getTotalPages();
        int currentPageIndex = noticeEntityPage.getNumber();

        Map<String, Object> noticePageInfo = new HashMap<>();

        noticePageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        noticePageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        noticePageInfo.put(KEY_CONTENT, noticeDTOList);

        return noticePageInfo;
    }
}
