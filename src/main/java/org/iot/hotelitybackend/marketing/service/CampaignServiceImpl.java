package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.iot.hotelitybackend.marketing.aggregate.CampaignSpecification;
import org.iot.hotelitybackend.marketing.dto.CampaignDTO;
import org.iot.hotelitybackend.marketing.repository.CampaignCustomerRepository;
import org.iot.hotelitybackend.marketing.repository.CampaignRepository;
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

import jakarta.persistence.criteria.Predicate;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final ModelMapper mapper;
    private final CampaignRepository campaignRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final CampaignCustomerRepository campaignCustomerRepository;

    @Autowired
    public CampaignServiceImpl(ModelMapper mapper, CampaignRepository campaignRepository, EmployeeRepository employeeRepository,
		CustomerRepository customerRepository, CampaignCustomerRepository campaignCustomerRepository) {
        this.mapper = mapper;
        this.campaignRepository = campaignRepository;
        this.employeeRepository = employeeRepository;
		this.customerRepository = customerRepository;
		this.campaignCustomerRepository = campaignCustomerRepository;
	}

    @Override
    public Map<String, Object> selectCampaignsList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Page<CampaignEntity> campaignPage = campaignRepository.findAll(pageable);
        List<CampaignDTO> campaignDTOList = campaignPage.stream().map(campaignEntity -> mapper.map(campaignEntity, CampaignDTO.class))
                .toList();

        int totalPagesCount = campaignPage.getTotalPages();
        int currentPageIndex = campaignPage.getNumber();

        Map<String, Object> campaignPageInfo = new HashMap<>();

        campaignPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        campaignPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        campaignPageInfo.put(KEY_CONTENT, campaignDTOList);

        return campaignPageInfo;
    }

    @Override
    public Map<String, Object> selectSearchedCampaignsList(
        int pageNum, String campaignSendType, Integer employeeCodeFk
        , Date campaignSentDate, Integer campaignCustomerPk, String customerName
        , String membershipName, String campaignName, Integer campaignSentStatus,
        Integer templatePk, Integer templateName
    ) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        Specification<CampaignEntity> spec = (root, query, criteriaBuilder) -> null;

        // if (campaignSendType != null && !campaignSendType.isEmpty()) {
        //     spec = spec.and(CampaignSpecification.equalsCampaignSendType(campaignSendType));
        // }
        //
        // if (employeeCodeFk != null) {
        //     spec = spec.and(CampaignSpecification.equalsEmployeeCode(employeeCodeFk));
        // }
        //
        // if (campaignSentDate != null) {
        //     spec = spec.and(CampaignSpecification.equalsCampaignSentDate(campaignSentDate));
        // }
        //
        // if(campaignCustomerPk != null){
        //     List<CampaignEntity> campaignEntities = campaignRepository.findAll();
        //     List<CampaignCustomerEntity> customerEntities = campaignCustomerRepository.findAllByCampaignCodeFk(campaignEntities.)
        //     List<CustomerEntity> customerEntities = customerRepository.findAllById(campaignCustomerPk).get();
        //
        //
        //     spec = spec.and(CampaignSpecification.likesCampaignCustomerPk(campaignCustomerPk));
        // }
        //
        // if(customerName != null){
        //     spec = spec.and(CampaignSpecification.likesCustomerName(customerName));
        // }
        //
        // if(campaignSentStatus != null){
        //     spec = spec.and(CampaignSpecification.likesCampaignSentStatus(campaignSentStatus));
        // }
        //
        // if(templatePk != null){
        //     spec = spec.and(CampaignSpecification.likesTemplatePk(templatePk));
        // }
        //
        // if(templateName != null){
        //     spec = spec.and(CampaignSpecification.likesTemplateName(templateName));
        // }


        Page<CampaignEntity> campaignEntityPage = campaignRepository.findAll(spec, pageable);
        List<CampaignDTO> campaignDTOList = campaignEntityPage
                .stream()
                .map(campaignEntity -> mapper.map(campaignEntity, CampaignDTO.class))
                .peek(campaignDTO -> campaignDTO.setEmployeeName(
                        mapper.map(employeeRepository.findById(campaignDTO.getEmployeeCodeFk()), EmployeeDTO.class).getEmployeeName()
                )).toList();

        int totalPagesCount = campaignEntityPage.getTotalPages();
        int currentPageIndex = campaignEntityPage.getNumber();

        Map<String, Object> campaignPageInfo = new HashMap<>();

        campaignPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
        campaignPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
        campaignPageInfo.put(KEY_CONTENT, campaignDTOList);

        return campaignPageInfo;
    }

    private Specification<CampaignEntity> buildOrPredicate(List<Specification<CampaignEntity>> specs) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = specs.stream()
                .map(spec -> spec.toPredicate(root, query, criteriaBuilder))
                .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }

}
