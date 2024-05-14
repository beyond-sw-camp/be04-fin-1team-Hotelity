package org.iot.hotelitybackend.customer.service;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.aggregate.CustomerSpecification;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.customer.repository.NationRepository;
import org.iot.hotelitybackend.sales.aggregate.MembershipEntity;
import org.iot.hotelitybackend.sales.aggregate.MembershipIssueEntity;
import org.iot.hotelitybackend.customer.aggregate.NationEntity;
import org.iot.hotelitybackend.sales.repository.MembershipIssueRepository;
import org.iot.hotelitybackend.sales.repository.MembershipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class CustomerServiceImpl implements CustomerService {
	private final ModelMapper mapper;
	private final CustomerRepository customerRepository;
	private final NationRepository nationRepository;
	private final MembershipRepository membershipRepository;
	private final MembershipIssueRepository membershipIssueRepository;

	@Autowired
	public CustomerServiceImpl(ModelMapper mapper, CustomerRepository customerRepository,
		NationRepository nationRepository,
		MembershipRepository membershipRepository, MembershipIssueRepository membershipIssueRepository) {
		this.mapper = mapper;
		this.customerRepository = customerRepository;
		this.nationRepository = nationRepository;
		this.membershipRepository = membershipRepository;
		this.membershipIssueRepository = membershipIssueRepository;
	}

	// @Override
	// public Map<String, Object> selectCustomersList(int pageNum, String customerType, String membershipLevelName) {
	//     Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
	//     Specification<CustomerEntity> spec = (root, query, criteriaBuilder) -> null;
	//     Specification<MembershipIssueEntity> spec1 = (root, query, criteriaBuilder) -> null;
	//
	//     if(!membershipLevelName.isEmpty()){
	//         int membershipLevelCodeFk = membershipRepository.findByMembershipLevelName(membershipLevelName).getMembershipLevelCodePk();
	//         // membershipIssueRepository.findByMembershipLevelCodeFk(membershipLevelCodePk).getCustomerCodeFk();
	//         spec1 = spec1.and(CustomerSpecification.equalsMembershipLevelCodeFk(membershipLevelCodeFk));
	//     }
	//     if (!customerType.isEmpty()){
	//         spec = spec.and(CustomerSpecification.equalsCustomerType(customerType));
	//     }
	//
	//     Page<CustomerEntity> customerPage = customerRepository.findAll(spec, pageable);
	//     MembershipIssueDTO membershipIssueDTO = membershipIssueRepository.findAll()
	//
	//     List<CustomerDTO> customerDTOList =
	//         customerPage.stream()
	//             .map(customerEntity -> mapper.map(customerEntity, CustomerDTO.class))
	//             .peek(customerDTO -> customerDTO.setNationName(nationRepository.findById(customerDTO.getNationCodeFk()).get().getNationName()))
	//             .peek(customerDTO -> customerDTO.setMembershipLevelName(membershipRepository.findById
	//                 (membershipIssueRepository.findByCustomerCodeFk(spec1, customerDTO.getCustomerCodePk()).getMembershipLevelCodeFk()).get().getMembershipLevelName()
	//             ))
	//             .toList();
	//
	//     int totalPagesCount = customerPage.getTotalPages();
	//     int currentPageIndex = customerPage.getNumber();
	//
	//     Map<String, Object> customerPageInfo = new HashMap<>();
	//
	//     customerPageInfo.put(KEY_TOTAL_PAGES_COUNT, totalPagesCount);
	//     customerPageInfo.put(KEY_CURRENT_PAGE_INDEX, currentPageIndex);
	//     customerPageInfo.put(KEY_CONTENT, customerDTOList);
	//
	//     return customerPageInfo;
	// }
	@Override
	public Map<String, Object> selectCustomersList(String customerType, String membershipLevelName, int page) {
		int fixedSize = 10;
		Pageable pageable = PageRequest.of(page, fixedSize, Sort.by("customerRegisteredDate").descending());

		Specification<CustomerEntity> spec = Specification.where(null);

		// 멤버십 레벨 이름으로 필터링
		if (!membershipLevelName.isEmpty()) {
			MembershipEntity membership = membershipRepository.findByMembershipLevelName(membershipLevelName);
			if (membership != null) {
				spec = spec.and(CustomerSpecification.equalsMembershipLevelName(membershipLevelName));
			}
		}

		// 고객 유형으로 필터링
		if (!customerType.isEmpty()) {
			spec = spec.and(CustomerSpecification.equalsCustomerType(customerType));
		}

		// 필터 조건에 따라 고객 정보 조회
		Page<CustomerEntity> customerPage = customerRepository.findAll(spec, pageable);
		List<CustomerDTO> customerDTOList = customerPage.stream()
			.map(customerEntity -> mapper.map(customerEntity, CustomerDTO.class))
			.peek(customerDTO -> {
				customerDTO.setNationName(nationRepository.findById(customerDTO.getNationCodeFk())
					.map(NationEntity::getNationName)
					.orElse(null));
				MembershipIssueEntity issue = membershipIssueRepository.findByCustomerCodeFk(
					customerDTO.getCustomerCodePk());
				customerDTO.setMembershipLevelName(membershipRepository.findById(issue.getMembershipLevelCodeFk())
					.map(MembershipEntity::getMembershipLevelName)
					.orElse(null));
			})
			.collect(Collectors.toList());

		Map<String, Object> customerPageInfo = new HashMap<>();
		customerPageInfo.put(KEY_TOTAL_PAGES_COUNT, customerPage.getTotalPages());
		customerPageInfo.put(KEY_CURRENT_PAGE_INDEX, customerPage.getNumber());
		customerPageInfo.put(KEY_CONTENT, customerDTOList);

		return customerPageInfo;
	}

	@Override
	public CustomerDTO selectCustomerByCustomerCodePk(int customerCodePk) {

		CustomerEntity customerEntity = customerRepository.findById(customerCodePk).get();
		return mapper.map(customerEntity, CustomerDTO.class);
	}
}