package org.iot.hotelitybackend.smpt;

import static java.time.LocalTime.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.iot.hotelitybackend.marketing.repository.CampaignCustomerRepository;
import org.iot.hotelitybackend.marketing.repository.CampaignRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

	private JavaMailSender mailSender;
	private static final String FROM_ADDRESS = "eodud3196@gs.cwnu.ac.kr";
	private final CustomerRepository customerRepository;
	private final CampaignRepository campaignRepository;
	private final CampaignCustomerRepository campaignCustomerRepository;

	@Override
	public Map<String, Object> mailsend(List<RequestDTO> requestDTOList) {
		SimpleMailMessage message = new SimpleMailMessage();

		for (RequestDTO dto : requestDTOList) {
			message.setTo(dto.getAddress());
			message.setSubject(dto.getTitle());
			message.setText(dto.getMessage());

			mailSender.send(message);

			CustomerEntity customer = customerRepository.findByCustomerEmail(dto.getAddress());
			CampaignEntity campaignEntity = CampaignEntity.builder()
				.campaignTitle(dto.getTitle())
				.campaignContent(dto.getMessage())
				.campaignSentDate(LocalDateTime.now())
				.campaignSendType("Email")
				.templateCodeFk(1)
				.employeeCodeFk(6)
				.build();

			campaignRepository.save(campaignEntity);

			CampaignCustomerEntity campaignCustomerEntity = CampaignCustomerEntity.builder()
				.campaignCodeFk(campaignEntity.getCampaignCodePk())
				.customerCodeFk(customer.getCustomerCodePk())
				// .reservationCodeFk(dto.getReservationFk())
				.build();
			campaignCustomerRepository.save(campaignCustomerEntity);
		}

		return null;
	}
}
