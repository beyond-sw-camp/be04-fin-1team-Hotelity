package org.iot.hotelitybackend.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.marketing.repository.TemplateRepository;
import org.iot.hotelitybackend.sales.aggregate.CouponEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.sales.repository.CouponIssueRepository;
import org.iot.hotelitybackend.sales.repository.CouponRepository;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.iot.hotelitybackend.sales.service.CouponIssueServiceImpl;
import org.iot.hotelitybackend.sales.vo.RequestCouponIssue;
import org.iot.hotelitybackend.smpt.EmailServiceImpl;
import org.iot.hotelitybackend.smpt.RequestDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsoleScheduler {

	private final CouponIssueServiceImpl couponIssueService;
	private final MembershipIssueRepository membershipIssueRepository;
	private final CouponRepository couponRepository;
	private final CouponIssueRepository couponIssueRepository;
	private final CustomerRepository customerRepository;
	private final TemplateRepository templateRepository;
	private final MembershipRepository membershipRepository;
	private final EmailServiceImpl emailService;

	private final AtomicBoolean hasRun = new AtomicBoolean(false);

	public ConsoleScheduler(CouponIssueServiceImpl couponIssueService, MembershipIssueRepository membershipIssueRepository,
		CouponRepository couponRepository, CouponIssueRepository couponIssueRepository,
		CustomerRepository customerRepository,
		TemplateRepository templateRepository, MembershipRepository membershipRepository, EmailServiceImpl emailService) {
		this.couponIssueService = couponIssueService;
		this.membershipIssueRepository = membershipIssueRepository;
		this.couponRepository = couponRepository;
		this.couponIssueRepository = couponIssueRepository;
		this.customerRepository = customerRepository;
		this.templateRepository = templateRepository;
		this.membershipRepository = membershipRepository;
		this.emailService = emailService;
	}

	// @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE) // 애플리케이션 시작 후 5초 뒤에 한 번 실행
	@Scheduled(cron = "0 0 9 1 3 ?") // 매년 3월 1일 09:00:00에 실행
	public void couponIssue() {
		if (hasRun.compareAndSet(false, true)) { // 이미 실행된 적이 있는지 체크
			System.out.println("Coupon issue started..."); // 로그 추가

			executeCouponIssue();

			System.out.println("Coupon issue completed."); // 로그 추가
			hasRun.set(false); // 다음 해에도 실행되도록 초기화
		}
	}

	private void executeCouponIssue() {
		couponIssueRepository.deleteAll();

		List<MembershipIssueEntity> membershipIssues = membershipIssueRepository.findAll();
		List<CouponEntity> coupons = couponRepository.findAll();
		List<RequestDTO> emailRequests = generateEmailRequests(membershipIssues, coupons);

		emailService.mailsend(emailRequests);
	}

	private List<RequestDTO> generateEmailRequests(List<MembershipIssueEntity> membershipIssues, List<CouponEntity> coupons) {
		List<RequestDTO> emailRequests = new ArrayList<>();

		for (MembershipIssueEntity membershipIssue : membershipIssues) {
			RequestDTO requestDTO = createEmailRequest(membershipIssue, coupons);
			emailRequests.add(requestDTO);
		}

		return emailRequests;
	}

	private RequestDTO createEmailRequest(MembershipIssueEntity membershipIssue, List<CouponEntity> coupons) {
		RequestDTO requestDTO = new RequestDTO();

		for (CouponEntity coupon : coupons) {
			if (membershipIssue.getMembershipLevelCodeFk() == coupon.getMembershipLevelCodeFk()) {
				assignCouponToCustomer(membershipIssue, coupon);
			}
		}

		String email = customerRepository.findById(membershipIssue.getCustomerCodeFk())
			.orElseThrow(() -> new IllegalArgumentException("Invalid customer ID"))
			.getCustomerEmail();

		String message = templateRepository.findById(5).orElseThrow(() -> new IllegalArgumentException("Invalid template ID")).toString();
		String title = generateEmailTitle(membershipIssue);

		requestDTO.setAddress(email);
		requestDTO.setTitle(title);
		requestDTO.setMessage(message);
		requestDTO.setEmployeeFk(6);
		requestDTO.setTemplateFk(5);
		requestDTO.setReservationFk(0);

		return requestDTO;
	}

	private void assignCouponToCustomer(MembershipIssueEntity membershipIssue, CouponEntity coupon) {
		RequestCouponIssue requestCouponIssue = new RequestCouponIssue();
		requestCouponIssue.setCouponCodeFk(coupon.getCouponCodePk());
		requestCouponIssue.setCustomerCodeFk(membershipIssue.getCustomerCodeFk());
		couponIssueService.registCouponToCustomer(requestCouponIssue);
	}

	private String generateEmailTitle(MembershipIssueEntity membershipIssue) {
		String membershipLevelName = membershipRepository.findById(
				membershipIssueRepository.findByCustomerCodeFk(membershipIssue.getCustomerCodeFk()).getMembershipLevelCodeFk())
			.orElseThrow(() -> new IllegalArgumentException("Invalid membership level code"))
			.getMembershipLevelName();

		return membershipLevelName + " 멤버십 등급 쿠폰 발급";
	}
}
