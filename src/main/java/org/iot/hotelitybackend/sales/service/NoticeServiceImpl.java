package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.employee.dto.EmployeeDTO;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.sales.aggregate.NoticeEntity;
import org.iot.hotelitybackend.sales.aggregate.NoticeSpecification;
import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.repository.NoticeRepository;
import org.iot.hotelitybackend.sales.vo.NoticeDashboardVO;
import org.iot.hotelitybackend.sales.vo.RequestModifyNotice;
import org.iot.hotelitybackend.sales.vo.RequestNotice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Service
public class NoticeServiceImpl implements NoticeService {

	private final ModelMapper mapper;
	private final NoticeRepository noticeRepository;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public NoticeServiceImpl(ModelMapper mapper, NoticeRepository noticeRepository,
		EmployeeRepository employeeRepository) {
		this.mapper = mapper;
		this.noticeRepository = noticeRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Map<String, Object> selectNoticesList(
		int pageNum, Integer noticeCodePk, String noticeTitle, String noticeContent,
		Integer employeeCodeFk, String employeeName, String branchCodeFk,
		LocalDateTime noticePostedDate, LocalDateTime noticeLastUpdatedDate,
		String orderBy, Integer sortBy) {

		Pageable pageable;

		if (orderBy == null) {
			pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("noticeCodePk").descending());
		} else {
			if (sortBy == 1) {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy));
			} else {
				pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(orderBy).descending());
			}
		}

		Specification<NoticeEntity> spec = (root, query, criteriaBuilder) -> null;

		// 공지코드
		if (noticeCodePk != null) {
			spec = spec.and(NoticeSpecification.equalsNoticeCodePk(noticeCodePk));
		}

		// 제목
		if (noticeTitle != null) {
			spec = spec.and(NoticeSpecification.likeNoticeTitle(noticeTitle));
		}

		// 내용
		if (noticeContent != null) {
			spec = spec.and(NoticeSpecification.likeNoticeContent(noticeContent));
		}

		// 직원코드
		if (employeeCodeFk != null) {
			spec = spec.and(NoticeSpecification.equalsEmployeeCodeFk(employeeCodeFk));
		}

		// 직원 이름
		if (employeeName != null) {
			spec = spec.and(NoticeSpecification.likeEmployeeName(employeeName));
		}

		// 지점코드
		if (branchCodeFk != null) {
			spec = spec.and(NoticeSpecification.equalsBranchCodeFk(branchCodeFk));
		}

		// 등록일
		if (noticePostedDate != null) {
			spec = spec.and(NoticeSpecification.equalsNoticePostedDate(noticePostedDate));
		}

		// 수정일
		if (noticeLastUpdatedDate != null) {
			spec = spec.and(NoticeSpecification.equalsNoticeLastUpdatedDate(noticeLastUpdatedDate));
		}

		Page<NoticeEntity> noticeEntityPage = noticeRepository.findAll(spec, pageable);

		List<NoticeDTO> noticeDTOList = noticeEntityPage
			.stream()
			.map(noticeEntity -> mapper.map(noticeEntity, NoticeDTO.class))
			.peek(noticeDTO -> noticeDTO.setPICEmployeeName(getEmployeeName(noticeDTO)))
			.toList();

		int totalPagesCount = noticeEntityPage.getTotalPages();
		int currentPageIndex = noticeEntityPage.getNumber();

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

		NoticeDTO noticeDTO = mapper.map(noticeEntity, NoticeDTO.class);

		String employeeName = getEmployeeName(noticeDTO);

		noticeDTO.setPICEmployeeName(employeeName);

		return noticeDTO;
	}

	private String getEmployeeName (NoticeDTO noticeDTO) {

		String employeeName = employeeRepository
			.findById(noticeDTO.getEmployeeCodeFk())
			.get()
			.getEmployeeName();

		return employeeName;
	}

	@Override
	public Map<String, Object> registNotice(RequestNotice requestNotice) {
		NoticeEntity noticeEntity = NoticeEntity.builder()
			.noticeTitle(requestNotice.getNoticeTitle())
			.noticeContent(requestNotice.getNoticeContent())
			.employeeCodeFk(requestNotice.getEmployeeCodeFk())
			.noticePostedDate(LocalDateTime.now())
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
			.noticeLastUpdatedDate(LocalDateTime.now())
			.branchCodeFk(requestModifyNotice.getBranchCodeFk())
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
	public Map<String, Object> selectLatestNoticeList() {
		List<NoticeEntity> noticeEntityList = noticeRepository.findTop3ByOrderByNoticeCodePkDesc();
		List<NoticeDashboardVO> noticeDashboardVOList = noticeEntityList
			.stream()
			.map(noticeEntity -> mapper.map(noticeEntity, NoticeDashboardVO.class))
			.peek(noticeDashboardVO -> noticeDashboardVO.setPICEmployeeName(
				employeeRepository.findById(
					noticeDashboardVO.getEmployeeCodeFk()).get().getEmployeeName()
			))
			.collect(Collectors.toList());

		// 공지 개수가 3개보다 부족할 때
		if (noticeDashboardVOList.size() < 3) {
			int limit = 3 - noticeDashboardVOList.size();
			for (int i = 0; i < limit; i++) {
				NoticeDashboardVO noticeDashboardVO = new NoticeDashboardVO();
				noticeDashboardVO.setNoticeTitle("공지가 없습니다.");
				noticeDashboardVOList.add(noticeDashboardVO);
			}
		}

		Map<String, Object> latestNoticeList = new HashMap<>();
		latestNoticeList.put(KEY_CONTENT, noticeDashboardVOList);

		return latestNoticeList;
	}

}
