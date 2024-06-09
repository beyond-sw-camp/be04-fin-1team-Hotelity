package org.iot.hotelitybackend.hotelservice.service;

import static java.util.stream.Collectors.*;
import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationSpecification;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.dto.StayDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.iot.hotelitybackend.hotelservice.repository.StayRepository;
import org.iot.hotelitybackend.hotelservice.vo.ReservationDashboardVO;
import org.iot.hotelitybackend.hotelservice.vo.ReservationSearchCriteria;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final ModelMapper mapper;
	private final CustomerRepository customerRepository;
	private final RoomRepository roomRepository;
	private final RoomCategoryRepository roomCategoryRepository;
	private final RoomLevelRepository roomLevelRepository;
	private final BranchRepository branchRepository;
	private final StayRepository stayRepository;

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository, ModelMapper mapper,
		CustomerRepository customerRepository, RoomRepository roomRepository,
		RoomCategoryRepository roomCategoryRepository, RoomLevelRepository roomLevelRepository,
		BranchRepository branchRepository, StayRepository stayRepository) {
		this.reservationRepository = reservationRepository;
		this.mapper = mapper;
		this.customerRepository = customerRepository;
		this.roomRepository = roomRepository;
		this.roomCategoryRepository = roomCategoryRepository;
		this.roomLevelRepository = roomLevelRepository;
		this.branchRepository = branchRepository;
		this.stayRepository = stayRepository;

		this.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		this.mapper.typeMap(ReservationEntity.class, ReservationDTO.class)
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getCustomer().getCustomerName(),
				ReservationDTO::setCustomerName
			))
			.addMappings(mapperNew -> mapperNew.map(
				src -> src.getCustomer().getCustomerEnglishName(),
				ReservationDTO::setCustomerEnglishName
			));;
	}

	/* 월별 예약 리스트 전체 조회 */
	@Override
	public Map<String, Object> selectReservationListByMonth(
		int year,
		int month,
		ReservationSearchCriteria criteria
	) {

		// 특정 월의 예약 내역을 조회하기 위해 월의 시작일과 종료일을 지정
		LocalDateTime startOfMonth =
			LocalDateTime.of(year, month, 1, 0, 0);
		System.out.println("해당 월의 시작 일자: " + startOfMonth);

		LocalDateTime endOfMonth =
			LocalDateTime.of(year, month, startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear()),
				23, 59, 59);
		System.out.println("해당 월의 마지막 일자: " + endOfMonth);

		// Specification 생성
		Specification<ReservationEntity> spec = buildSpecification(criteria, startOfMonth, endOfMonth);

		String orderBy = criteria.getOrderBy();
		Integer sortBy = criteria.getSortBy();

		// 특정 월에 해당하는 예약 내역 리스트 조회
		List<ReservationEntity> reservationEntityList =
			reservationRepository.findAll(spec);
		// reservationRepository.findByReservationCheckinDateBetween(startOfMonth, endOfMonth);

		List<ReservationDTO> reservationDTOList = setDTOField(reservationEntityList);

		// Pageable 쓰지않는 List 정렬
		if (orderBy == null) {
			reservationDTOList.stream().sorted(
					Comparator.comparing(ReservationDTO::getReservationCheckinDate).reversed())
				.collect(toList());
		} else {
			reservationDTOList = sortList(reservationDTOList, orderBy, sortBy);
		}

		Map<String, Object> reservationListInfo = new HashMap<>();

		reservationListInfo.put(KEY_CONTENT, reservationDTOList);

		return reservationListInfo;
	}

	private Specification<ReservationEntity> buildSpecification(
		ReservationSearchCriteria criteria,
		LocalDateTime startOfMonth,
		LocalDateTime endOfMonth
	) {
		Integer reservationCodePk = criteria.getReservationCodePk();
		Integer customerCodeFk = criteria.getCustomerCodeFk();
		String customerName = criteria.getCustomerName();
		String customerEnglishName = criteria.getCustomerEnglishName();
		String roomCodeFk = criteria.getRoomCodeFk();
		String roomName = criteria.getRoomName();
		String roomLevelName = criteria.getRoomLevelName();
		Integer roomCapacity = criteria.getRoomCapacity();
		String branchCodeFk = criteria.getBranchCodeFk();
		LocalDateTime reservationDate = criteria.getReservationDate();
		LocalDateTime reservationCheckinDate = criteria.getReservationCheckinDate();
		LocalDateTime reservationCheckoutDate = criteria.getReservationCheckoutDate();
		Integer reservationCancelStatus = criteria.getReservationCancelStatus();

		Specification<ReservationEntity> spec =
			Specification.where(ReservationSpecification.betweenDate(startOfMonth, endOfMonth));

		// 예약코드
		if (reservationCodePk != null) {
			spec = spec.and(ReservationSpecification.equalsReservationCodePk(reservationCodePk));
		}
		// 고객코드
		if (customerCodeFk != null) {
			spec = spec.and(ReservationSpecification.equalsCustomerCodeFk(customerCodeFk));
		}
		// 한글이름
		if (customerName != null) {
			spec = spec.and(ReservationSpecification.likeCustomerName(customerName));
		}
		// 영어이름
		if (customerEnglishName != null) {
			spec = spec.and(ReservationSpecification.likeCustomerEnglishName(customerEnglishName));
		}
		// 객실 코드
		if (roomCodeFk != null) {
			spec = spec.and(ReservationSpecification.likeRoomCodeFk(roomCodeFk));
		}
		// 객실명
		if (roomName != null) {
			spec = spec.and(ReservationSpecification.likeRoomName(roomName));
		}
		// 객실등급명
		if (roomLevelName != null) {
			spec = spec.and(ReservationSpecification.likeRoomLevelName(roomLevelName));
		}
		// 객실수용인원
		if (roomCapacity != null) {
			spec = spec.and(ReservationSpecification.equalsRoomCapacity(roomCapacity));
		}
		// 지점코드
		if (branchCodeFk != null) {
			spec = spec.and(ReservationSpecification.equalsBranchCodeFk(branchCodeFk));
		}
		// 예약일자
		if (reservationDate != null) {
			spec = spec.and(ReservationSpecification.equalsReservationDate(reservationDate));
		}
		// 체크인일자
		if (reservationCheckinDate != null) {
			spec = spec.and(ReservationSpecification.equalsCheckinDate(reservationCheckinDate));
		}
		// 체크아웃일자
		if (reservationCheckoutDate != null) {
			spec = spec.and(ReservationSpecification.equalsCheckoutDate(reservationCheckoutDate));
		}
		// 예약취소여부
		if (reservationCancelStatus != null) {
			spec = spec.and(ReservationSpecification.equalsReservationCancleStatus(reservationCancelStatus));
		}
		return spec;
	}

	private List<ReservationDTO> sortList(List<ReservationDTO> list, String orderBy, Integer sortBy) {

		Comparator<ReservationDTO> comparator;

		switch (orderBy) {
			case "reservationCodePk":
				comparator = Comparator.comparing(ReservationDTO::getReservationCodePk);
				break;
			case "customerCodeFk":
				comparator = Comparator.comparing(ReservationDTO::getCustomerCodeFk);
				break;
			case "customerName":
				comparator = Comparator.comparing(ReservationDTO::getCustomerName);
				break;
			case "customerEnglishName":
				comparator = Comparator.comparing(ReservationDTO::getCustomerEnglishName);
				break;
			case "roomCodeFk":
				comparator = Comparator.comparing(ReservationDTO::getRoomCodeFk);
				break;
			case "roomLevelName":
				comparator = Comparator.comparing(ReservationDTO::getRoomLevelName);
				break;
			case "roomCapacity":
				comparator = Comparator.comparing(ReservationDTO::getRoomCapacity);
				break;
			case "branchCodeFk":
				comparator = Comparator.comparing(ReservationDTO::getBranchCodeFk);
				break;
			case "reservationDate":
				comparator = Comparator.comparing(ReservationDTO::getReservationDate);
				break;
			case "reservationCheckinDate":
				comparator = Comparator.comparing(ReservationDTO::getReservationCheckinDate);
				break;
			case "reservationCheckoutDate":
				comparator = Comparator.comparing(ReservationDTO::getReservationCheckoutDate);
				break;
			case "reservationCancelStatus":
				comparator = Comparator.comparing(ReservationDTO::getReservationCancelStatus);
				break;
			case "reservationPersonnel":
				comparator = Comparator.comparing(ReservationDTO::getReservationPersonnel);
				break;
			default:
				throw new IllegalArgumentException(orderBy + "에 해당하는 필드가 존재하지 않습니다.");
		}

		if (sortBy != 1) {
			comparator = comparator.reversed();
		}

		return list.stream().sorted(comparator).collect(toList());
	}

	/* 예약 코드로 특정 예약 내역 조회 */
	@Override
	public Map<String, Object> selectReseravtionInfoByReservationCodePk(Integer reservationCodePk) {

		List<ReservationEntity> reservationEntityList =
			reservationRepository.findById(reservationCodePk).stream().toList();

		List<ReservationDTO> reservationDTOList = setDTOField(reservationEntityList);

		Map<String, Object> reservationInfo = new HashMap<>();

		reservationInfo.put(KEY_CONTENT, reservationDTOList);

		return reservationInfo;
	}

	/* 일자별 예약 리스트 조회 */
	@Override
	public Map<String, Object> selectReservationListByDay(LocalDateTime reservationCheckDate) {
		List<ReservationEntity> dailyReservationEntityList = reservationRepository.findByReservationCheckinDate(
			reservationCheckDate);

		List<ReservationDTO> dailyReservationDTOList = setDTOField(dailyReservationEntityList);

		Map<String, Object> dailyReservationInfo = new HashMap<>();

		dailyReservationInfo.put(KEY_CONTENT, dailyReservationDTOList);

		return dailyReservationInfo;
	}

	@Override
	public Map<String, Object> selectReservationsByYear(Integer yearInput) {

		// 특정 월의 예약 내역을 조회하기 위해 월의 시작일과 종료일을 지정
		LocalDateTime startOfYear =
			LocalDateTime.of(yearInput, 1, 1, 0, 0);
		System.out.println("해당 년도의 시작 일자: " + startOfYear);

		LocalDateTime endOfYear =
			LocalDateTime.of(yearInput, 12, startOfYear.getMonth().length(startOfYear.toLocalDate().isLeapYear()),
				23, 59, 59);
		System.out.println("해당 년도의 마지막 일자: " + endOfYear);

		Specification<ReservationEntity> spec =
			Specification.where(ReservationSpecification.betweenDate(startOfYear, endOfYear));

		List<ReservationEntity> reservationEntityList = reservationRepository.findAll(spec);
		List<ReservationDTO> reservationDTOList = reservationEntityList
			.stream()
			.map(reservationEntity -> mapper.map(reservationEntity, ReservationDTO.class))
			.toList();

		Map<String, Object> reservationInfo = new HashMap<>();
		reservationInfo.put(KEY_CONTENT, reservationDTOList);

		return reservationInfo;
	}

	@Override
	public Map<String, Object> selectLatestReservationList() {
		List<ReservationEntity> reservationEntityList = reservationRepository.findTop3ByOrderByReservationDateDesc();
		List<ReservationDashboardVO> reservationDTOList = reservationEntityList
			.stream()
			.map(reservationEntity -> mapper.map(reservationEntity, ReservationDashboardVO.class))
			.peek(reservationDashboardVO -> reservationDashboardVO.setCustomerName(
				customerRepository.findById(
					reservationDashboardVO.getCustomerCodeFk()
				).get().getCustomerName()
			))
			.peek(reservationDashboardVO -> reservationDashboardVO.setRoomName(
				roomCategoryRepository.findById(
					roomRepository.findById(
						reservationDashboardVO.getRoomCodeFk()
					).get().getRoomCategoryCodeFk()
				).get().getRoomName()
			))
			.peek(reservationDashboardVO -> reservationDashboardVO.setRoomLevelName(
				roomLevelRepository.findById(
					roomCategoryRepository.findById(
						roomRepository.findById(
							reservationDashboardVO.getRoomCodeFk()
						).get().getRoomCategoryCodeFk()
					).get().getRoomLevelCodeFk()
				).get().getRoomLevelName()
			))
			.collect(Collectors.toList());

		// 예약 수가 3개보다 부족할 때
		if(reservationDTOList.size() < 3) {
			int limit = 3 - reservationDTOList.size();
			for(int i = 0; i < limit; i++) {
				ReservationDashboardVO emptyNotice = new ReservationDashboardVO();
				emptyNotice.setCustomerName("예약이 없습니다.");
				reservationDTOList.add(emptyNotice);
			}
		}

		Map<String, Object> reservationInfo = new HashMap<>();
		reservationInfo.put(KEY_CONTENT, reservationDTOList);

		return reservationInfo;
	}

	/* fk 값들의 이름을 가져오는 코드 */
	public List<ReservationDTO> setDTOField(List<ReservationEntity> reservationEntityList) {

		AtomicReference<List<StayEntity>> existingStayEntity = new AtomicReference<>(new ArrayList<>());

		List<ReservationDTO> list =
			reservationEntityList.stream().map(reservationEntity -> {
				ReservationDTO reservationDTO = mapper.map(reservationEntity, ReservationDTO.class);

				// 숙박 일수 계산
				reservationDTO.setStayPeriod(calculateStayPeriod(reservationEntity.getReservationCodePk()));

				// 고객명
				reservationDTO.setCustomerName(
					mapper.map(customerRepository.findById(reservationDTO.getCustomerCodeFk()).orElse(null),
						CustomerDTO.class).getCustomerName());

				// 객실명
				reservationDTO.setRoomName(String.valueOf(roomCategoryRepository.findById(
					roomRepository.findById(reservationDTO.getRoomCodeFk()).get().getRoomCategoryCodeFk()
				).get().getRoomName()));

				// 객실등급명
				reservationDTO.setRoomLevelName(
					roomLevelRepository.findById(
						roomCategoryRepository.findById(
							roomRepository.findById(
								reservationDTO.getRoomCodeFk()
							).get().getRoomCategoryCodeFk()
						).get().getRoomLevelCodeFk()
					).get().getRoomLevelName());

				// 객실 수용 인원
				reservationDTO.setRoomCapacity(
					roomCategoryRepository.findById(
						roomRepository.findById(
							reservationDTO.getRoomCodeFk()
						).get().getRoomCategoryCodeFk()
					).get().getRoomCapacity());

				// 투숙 상태
				existingStayEntity.set(stayRepository.findByReservationCodeFk(reservationDTO.getReservationCodePk())
					.stream()
					.toList());
				reservationDTO.setStayStatus(existingStayEntity.get().isEmpty() ? 0 : 1);

				return reservationDTO;
			}).collect(Collectors.toList());

		return list;
	}

	/* 숙박 일수 계산 */
	public String calculateStayPeriod(Integer reservationCodePk){
		ReservationEntity reservationEntity = reservationRepository.findById(reservationCodePk).orElse(null);

		LocalDateTime checkinDate = reservationEntity.getReservationCheckinDate();
		LocalDateTime checkoutDate = reservationEntity.getReservationCheckoutDate();

		Integer result = checkoutDate.getDayOfYear() - checkinDate.getDayOfYear();
		return result + "박";
	}
}
