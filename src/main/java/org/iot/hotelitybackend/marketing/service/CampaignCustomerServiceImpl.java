package org.iot.hotelitybackend.marketing.service;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;
import org.iot.hotelitybackend.marketing.repository.CampaignCustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignCustomerServiceImpl implements CampaignCustomerService{

    private final ModelMapper mapper;
    private final CampaignCustomerRepository campaignCustomerRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CampaignCustomerServiceImpl(ModelMapper mapper, CampaignCustomerRepository campaignCustomerRepository, CustomerRepository customerRepository) {
        this.mapper = mapper;
        this.campaignCustomerRepository = campaignCustomerRepository;
        this.customerRepository = customerRepository;
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
}
