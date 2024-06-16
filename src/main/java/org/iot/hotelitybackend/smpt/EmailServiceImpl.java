package org.iot.hotelitybackend.smpt;

import static java.time.LocalTime.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.marketing.aggregate.CampaignCustomerEntity;
import org.iot.hotelitybackend.marketing.aggregate.CampaignEntity;
import org.iot.hotelitybackend.marketing.repository.CampaignCustomerRepository;
import org.iot.hotelitybackend.marketing.repository.CampaignRepository;
import org.iot.hotelitybackend.marketing.repository.TemplateRepository;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.sales.aggregate.VocEntity;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender mailSender;
	private static final String FROM_ADDRESS = "eodud3196@gs.cwnu.ac.kr";
	private final CustomerRepository customerRepository;
	private final CampaignRepository campaignRepository;
	private final CampaignCustomerRepository campaignCustomerRepository;
	private final MembershipIssueRepository membershipIssueRepository;
	private final MembershipRepository membershipRepository;
	private final TemplateRepository templateRepository;

	@Autowired
	public EmailServiceImpl(JavaMailSender mailSender, CustomerRepository customerRepository,
		CampaignRepository campaignRepository, CampaignCustomerRepository campaignCustomerRepository,
		MembershipIssueRepository membershipIssueRepository, MembershipRepository membershipRepository,
		TemplateRepository templateRepository) {
		this.mailSender = mailSender;
		this.customerRepository = customerRepository;
		this.campaignRepository = campaignRepository;
		this.campaignCustomerRepository = campaignCustomerRepository;
		this.membershipIssueRepository = membershipIssueRepository;
		this.membershipRepository = membershipRepository;
		this.templateRepository = templateRepository;
	}

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

	@Override
	public Map<String, Object> mailsendByMembershipLevel(RequestSendMailByLevelDTO requestSendMailByLevelDTO) {

		// 메일 발송할 고객 리스트 가져오기
		List<MembershipIssueEntity> membershipIssueEntityList = membershipIssueRepository.findAllByMembershipLevelCodeFk(
			membershipRepository.findByMembershipLevelName(requestSendMailByLevelDTO.getMembershipLevelName()).getMembershipLevelCodePk()
		);
		List<CustomerEntity> customerEntityList = membershipIssueEntityList
			.stream()
			.map(membershipIssueEntity -> customerRepository.findById(membershipIssueEntity.getCustomerCodeFk()).get())
			.toList();

		// 캠페인 생성 후 campaign_tb 에 save
		CampaignEntity campaignEntity = CampaignEntity.builder()
			.campaignSendType(requestSendMailByLevelDTO.getSendType())
			.campaignContent(requestSendMailByLevelDTO.getMessageContent())
			.campaignSentDate(requestSendMailByLevelDTO.getMailSendDate())
			.campaignSentStatus(1)
			.employeeCodeFk(requestSendMailByLevelDTO.getEmployeeCode())
			.templateCodeFk(requestSendMailByLevelDTO.getTemplateCode())
			.campaignTitle(requestSendMailByLevelDTO.getTitle())
			.build();
		Integer campaignCodePk = campaignRepository.save(campaignEntity).getCampaignCodePk();

		// 메일 발송 객체 생성
		SimpleMailMessage message = new SimpleMailMessage();

		for (CustomerEntity customer : customerEntityList) {

			// 메일 발송 객체 완성시켜 메일 발송
			message.setTo(customer.getCustomerEmail());
			message.setSubject(requestSendMailByLevelDTO.getTitle());
			message.setText(
				customer.getCustomerName() +
					"님, 안녕하세요. Hotelity 입니다. " +
					campaignEntity.getCampaignContent()
			);
			mailSender.send(message);

			// 캠페인 발송 객체 생성 후 campaign_customer_tb 에 save
			CampaignCustomerEntity campaignCustomerEntity = CampaignCustomerEntity.builder()
				.campaignCodeFk(campaignCodePk)
				.customerCodeFk(customer.getCustomerCodePk())
				.build();
			campaignCustomerRepository.save(campaignCustomerEntity);
		}

		Map<String, Object> sendResult = new HashMap<>();
		sendResult.put(
			"result",
			"Completed mail send transaction to " + customerEntityList.size() + " customers with membership level " + requestSendMailByLevelDTO.getMembershipLevelName()
		);

		return sendResult;
	}

	@Override
	public void sendVocProcessedEmail(VocEntity vocEntity) {
		SimpleMailMessage message = new SimpleMailMessage();

		CustomerEntity customer = customerRepository.findById(vocEntity.getCustomerCodeFk()).get();

		String emailTitle = "[Hotelity 문의 처리 완료]";
		String emailContent = customer.getCustomerName() +
			"님, 안녕하세요. Hotelity 입니다.<br>" +
			"고객님의 VOC 처리가 완료되었습니다." +
			"\n\n문의내역 : " + vocEntity.getVocTitle() +
			"\n문의일자 : " + vocEntity.getVocCreatedDate() +
			"\n처리상태 : 처리완료" +
			"\n문의내용 : " + vocEntity.getVocContent() +
			"\n처리내용 : " + vocEntity.getVocResponse() +
			"\n\nHotelity를 이용해주셔서 감사합니다. 더욱 좋은 서비스로 보답하겠습니다.";

		message.setTo(customer.getCustomerEmail());
		message.setSubject(emailTitle);
		message.setText(emailContent);

		mailSender.send(message);

		CampaignEntity campaignEntity = CampaignEntity.builder()
				.campaignSendType("Email")
				.campaignTitle(emailTitle)
				.campaignContent(emailContent)
				.campaignSentDate(LocalDateTime.now())
				.campaignSentStatus(1)
				.employeeCodeFk(vocEntity.getEmployeeCodeFk())
				.build();

		Integer campaignCodePk = campaignRepository.save(campaignEntity).getCampaignCodePk();

		CampaignCustomerEntity campaignCustomerEntity = CampaignCustomerEntity.builder()
				.campaignCodeFk(campaignCodePk)
				.customerCodeFk(customer.getCustomerCodePk())
				.build();

		campaignCustomerRepository.save(campaignCustomerEntity);
	}
}
