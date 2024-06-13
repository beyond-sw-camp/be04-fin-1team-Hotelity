package org.iot.hotelitybackend.marketing.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerSpecification;
import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.iot.hotelitybackend.marketing.aggregate.TemplateEntity;
import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;
import org.iot.hotelitybackend.marketing.repository.CampaignCustomerRepository;
import org.iot.hotelitybackend.marketing.repository.CampaignRepository;
import org.iot.hotelitybackend.marketing.repository.TemplateRepository;
import org.iot.hotelitybackend.marketing.vo.CampaignCustomerSearchCriteria;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

@Service
public class CampaignCustomerServiceImpl implements CampaignCustomerService{

    private final ModelMapper mapper;
    private final CampaignCustomerRepository campaignCustomerRepository;
    private final CustomerRepository customerRepository;
    private final CampaignRepository campaignRepository;
    private final TemplateRepository templateRepository;
    private final EmployeeRepository employeeRepository;
    private final MembershipIssueRepository membershipIssueRepository;
    private final MembershipRepository membershipRepository;

    @Autowired
    public CampaignCustomerServiceImpl(ModelMapper mapper, CampaignCustomerRepository campaignCustomerRepository, CustomerRepository customerRepository,
		CampaignRepository campaignRepository, TemplateRepository templateRepository,
		EmployeeRepository employeeRepository, MembershipIssueRepository membershipIssueRepository,
		MembershipRepository membershipRepository) {
        this.mapper = mapper;
        this.campaignCustomerRepository = campaignCustomerRepository;
        this.customerRepository = customerRepository;
		this.campaignRepository = campaignRepository;
		this.templateRepository = templateRepository;
		this.employeeRepository = employeeRepository;
		this.membershipIssueRepository = membershipIssueRepository;
		this.membershipRepository = membershipRepository;
	}

    @Override
    public CampaignCustomerDTO selectCampaignByCampaignSentCustomerCodePk(int campaignSentCustomerCodePk) {
        CampaignCustomerEntity campaignCustomerEntity = campaignCustomerRepository.findById(campaignSentCustomerCodePk)
                .orElseThrow(IllegalArgumentException::new);

        String customerName = customerRepository
                .findById(campaignCustomerEntity.getCustomerCodeFk())
                .get()
                .getCustomerName();

        CampaignCustomerDTO campaignCustomerDTO = mapper.map(campaignCustomerEntity, CampaignCustomerDTO.class);

        campaignCustomerDTO.setCustomerName(customerName);
        
        return campaignCustomerDTO;
    }

    @Override
    public Map<String, Object> selectSearchedCampaignsList(CampaignCustomerSearchCriteria criteria) {

        Integer pageNum = criteria.getPageNum();
        String orderBy = criteria.getOrderBy();
        Integer sortBy = criteria.getSortBy();

        Pageable pageable;
        if(orderBy == null){
            pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("campaignCodeFk"));
        } else{
            if (sortBy==1){
                pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
            }
            else{
                pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
            }
        }

        Specification<CampaignCustomerEntity> spec = buildSpecification(criteria);

        Page<CampaignCustomerEntity> campaignCustomerEntityPage = campaignCustomerRepository.findAll(spec, pageable);
        List<CampaignCustomerDTO> campaignCustomerDTOList = campaignCustomerEntityPage
            .stream()
            .map(campaignCustomerEntity -> mapper.map(campaignCustomerEntity, CampaignCustomerDTO.class))
            .peek(campaignCustomerDTO -> {
                CampaignEntity campaignEntity = campaignRepository.findById(
                    campaignCustomerDTO.getCampaignCodeFk()).get();
                campaignCustomerDTO.setCustomerName(
                    customerRepository.findById(
                        campaignCustomerDTO.getCustomerCodeFk()
                    ).get().getCustomerName()
                );
                campaignCustomerDTO.setCampaignSendType(
                    campaignEntity.getCampaignSendType()
                );
                campaignCustomerDTO.setCampaignContent(
                    campaignEntity.getCampaignContent()
                );
                campaignCustomerDTO.setCampaignSentDate(
                    campaignEntity.getCampaignSentDate()
                );
                campaignCustomerDTO.setCampaignSentStatus(
                    campaignEntity.getCampaignSentStatus()
                );
                campaignCustomerDTO.setTemplateCodeFk(
                    campaignEntity.getTemplateCodeFk()
                );

                if (campaignEntity.getTemplateCodeFk() != null) {
                    templateRepository.findById(campaignEntity.getTemplateCodeFk()).ifPresent(
                            template -> campaignCustomerDTO.setTemplateName(template.getTemplateName())
                    );
                }

                campaignCustomerDTO.setEmployeeName(
                    employeeRepository.findById(
                        campaignEntity.getEmployeeCodeFk()
                    ).get().getEmployeeName()
                );
                campaignCustomerDTO.setCampaignTitle(
                    campaignEntity.getCampaignTitle()
                );
                campaignCustomerDTO.setMembershipLevelName(
                    membershipRepository.findById(
                        membershipIssueRepository.findByCustomerCodeFk(
                            campaignCustomerDTO.getCustomerCodeFk()
                        ).getMembershipLevelCodeFk()
                    ).get().getMembershipLevelName()
                );
            })
            .toList();

        Map<String, Object> campaignCustomerPageInfo = new HashMap<>();
        campaignCustomerPageInfo.put(KEY_TOTAL_PAGES_COUNT, campaignCustomerEntityPage.getTotalPages());
        campaignCustomerPageInfo.put(KEY_CURRENT_PAGE_INDEX, campaignCustomerEntityPage.getNumber());
        campaignCustomerPageInfo.put(KEY_CONTENT, campaignCustomerDTOList);

        return campaignCustomerPageInfo;
    }

    private Specification<CampaignCustomerEntity> buildSpecification(CampaignCustomerSearchCriteria criteria) {
        Integer pageNum = criteria.getPageNum();
        Integer campaignCodeFk = criteria.getCampaignCodeFk();
        String campaignSendType = criteria.getCampaignSendType();
        LocalDateTime campaignSentDate = criteria.getCampaignSentDate();
        String customerName = criteria.getCustomerName();
        String campaignTitle = criteria.getCampaignTitle();
        Integer campaignSentStatus = criteria.getCampaignSentStatus();
        Integer templateCodeFk = criteria.getTemplateCodeFk();
        String templateName = criteria.getTemplateName();

        Specification<CampaignCustomerEntity> spec = (root, query, criteriaBuilder) -> null;

        if(campaignCodeFk != null){
            spec = spec.and(CampaignCustomerSpecification.equalsCampaignCodeFk(campaignCodeFk));
        }
        if (campaignSendType != null) {
            spec = spec.and(CampaignCustomerSpecification.equalsCampaignSendType(campaignSendType));
        }
        if (campaignSentDate != null) {
            spec = spec.and(CampaignCustomerSpecification.equalsCampaignSentDate(campaignSentDate));
        }
        if(customerName != null){
            spec = spec.and(CampaignCustomerSpecification.likesCustomerName(customerName));
        }
        if(campaignSentStatus != null){
            spec = spec.and(CampaignCustomerSpecification.likesCampaignSentStatus(campaignSentStatus));
        }
        if(templateCodeFk != null){
            spec = spec.and(CampaignCustomerSpecification.likesTemplateFk(templateCodeFk));
        }
        if(templateName != null){
            spec = spec.and(CampaignCustomerSpecification.likesTemplateName(templateName));
        }
        if(campaignTitle != null){
            spec = spec.and(CampaignCustomerSpecification.likesCampaignTitle(campaignTitle));
        }
        return spec;
    }
}
