package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.iot.hotelitybackend.hotelservice.repository.PaymentRepository;
import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MembershipIssueServiceImpl implements MembershipIssueService{

    private final ModelMapper mapper;
    private final MembershipIssueRepository membershipIssueRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipIssueServiceImpl(ModelMapper mapper, MembershipIssueRepository membershipIssueRepository, PaymentRepository paymentRepository, CustomerRepository customerRepository, MembershipRepository membershipRepository) {
        this.mapper = mapper;
        this.membershipIssueRepository = membershipIssueRepository;
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.membershipRepository = membershipRepository;
    }

    @Override
    public void assignMembershipBasedOnPayments() {
        // 기간 설정: 작년 3월 1일부터 올해 2월 28일까지
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.add(Calendar.YEAR, -1);
        Date startDate = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, 29);
        Date endDate = cal.getTime();

        // 모든 고객 조회
        List<CustomerEntity> customers = customerRepository.findAll();

        for (CustomerEntity customer : customers) {
            // 특정 기간 동안의 결제 내역 조회
            List<PaymentEntity> payments = paymentRepository.findByCustomerCodeFkAndPaymentDateBetween(customer.getCustomerCodePk(), startDate, endDate);

            // 총 결제 금액 계산
            double totalPayment = payments.stream()
                    .mapToDouble(PaymentEntity::getPaymentAmount)
                    .sum();

            // 멤버십 등급 부여 로직
            Integer newMembershipLevelCodeFk = determineMembershipLevelCodeFk(totalPayment);

            // 기존 멤버십 정보 업데이트 또는 새로 부여
            MembershipIssueEntity membershipIssue = membershipIssueRepository.findTopByCustomerCodeFkOrderByMembershipIssueDateDesc(customer.getCustomerCodePk());
            if (membershipIssue == null) {
                membershipIssue = MembershipIssueEntity.builder()
                        .customerCodeFk(customer.getCustomerCodePk())
                        .membershipLevelCodeFk(newMembershipLevelCodeFk)
                        .membershipIssueDate(new Date())
                        .build();
            } else {
                membershipIssue.setMembershipLevelCodeFk(newMembershipLevelCodeFk); // 여기 수정
                membershipIssue.setMembershipIssueDate(new Date());
            }

            membershipIssueRepository.save(membershipIssue);
        }
    }

    private Integer determineMembershipLevelCodeFk(double totalPayment) {
        // 총 결제 금액에 따라 멤버십 등급을 결정
        if (totalPayment >= 100000000) {
            return 5;
        } else if (totalPayment >= 60000000) {
            return 4;
        } else if (totalPayment >= 30000000) {
            return 3;
        } else if (totalPayment >= 10000000) {
            return 2;
        } else {
            return 1;
        }
    }
}
