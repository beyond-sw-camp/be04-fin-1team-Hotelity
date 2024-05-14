package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.dto.RoomCategoryDTO;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.iot.hotelitybackend.common.constant.Constant.*;

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
    public Map<String, Object> selectSearchedVocsList(int pageNum, String branchCodeFk, Integer vocProcessStatus, String vocCategory, Date vocCreatedDate) {
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

        Page<VocEntity> vocEntityPage = vocRepository.findAll(spec, pageable);
        List<VocDTO> vocDTOList = vocEntityPage
                .stream()
                .map(vocEntity -> mapper.map(vocEntity, VocDTO.class))
                .toList();

        int totalPagesCount = vocEntityPage.getTotalPages();
        int currentPageIndex = vocEntityPage.getNumber();

        Map<String, Object> vocPageInfo = new HashMap<>();

        vocPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        vocPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        vocPageInfo.put(KEY_CONTENT, vocDTOList);

        return vocPageInfo;
    }
}
