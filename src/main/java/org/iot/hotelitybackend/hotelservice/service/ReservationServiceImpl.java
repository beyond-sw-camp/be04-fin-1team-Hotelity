package org.iot.hotelitybackend.hotelservice.service;

import static org.iot.hotelitybackend.common.constant.Constant.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationSpecification;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository, ModelMapper mapper,
		CustomerRepository customerRepository, RoomRepository roomRepository,
		RoomCategoryRepository roomCategoryRepository,
		RoomLevelRepository roomLevelRepository, BranchRepository branchRepository) {
		this.reservationRepository = reservationRepository;
		this.mapper = mapper;
		this.customerRepository = customerRepository;
		this.roomRepository = roomRepository;
		this.roomCategoryRepository = roomCategoryRepository;
		this.roomLevelRepository = roomLevelRepository;
		this.branchRepository = branchRepository;
	}

	/* 월별 예약 리스트 전체 조회 */
	@Override
	public Map<String, Object> selectReservationListByMonth(
		int year, int month, Integer reservationCodePk,
		Integer customerCodeFk, String customerName,
		String customerEnglishName, String roomCodeFk,
		String roomName, String roomLevelName,
		Integer roomCapacity, String branchCodeFk,
		LocalDateTime reservationDate,
		LocalDateTime reservationCheckinDate,
		LocalDateTime reservationCheckoutDate,
		Integer reservationCancelStatus,
		String orderBy, Integer sortBy) {

		// 특정 월의 예약 내역을 조회하기 위해 월의 시작일과 종료일을 지정
		LocalDateTime startOfMonth =
			LocalDateTime.of(year, month, 1, 0, 0);
		System.out.println("해당 월의 시작 일자: " + startOfMonth);

		LocalDateTime endOfMonth =
			LocalDateTime.of(year, month, startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear()),
				23, 59, 59);
		System.out.println("해당 월의 마지막 일자: " + endOfMonth);

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

		// 특정 월에 해당하는 예약 내역 리스트 조회
		List<ReservationEntity> reservationEntityList =
			reservationRepository.findAll(spec);
		// reservationRepository.findByReservationCheckinDateBetween(startOfMonth, endOfMonth);

		List<ReservationDTO> reservationDTOList = setDTOField(reservationEntityList);

		// Pageable 쓰지않는 List 정렬
		if (orderBy == null) {
			reservationDTOList.stream().sorted(
					Comparator.comparing(ReservationDTO::getReservationCheckinDate).reversed())
				.collect(Collectors.toList());
		} else {
			reservationDTOList = sortList(reservationDTOList, orderBy, sortBy);
		}

		Map<String, Object> reservationListInfo = new HashMap<>();

		reservationListInfo.put(KEY_CONTENT, reservationDTOList);

		return reservationListInfo;
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

		return list.stream().sorted(comparator).collect(Collectors.toList());
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

	/* 체크인 시 체크인 상태 변경 */
	// 예약코드로 투숙 내역 조회하여 없으면 체크인 추가(투숙쪽에 작성된 메서드 활용)
	// 투숙  등록 시 ReservationDTO의 reservationCheckinStatus 를 1로 변경
	// ReservationDTO 데이터 매핑(reservationCheckinStatus)

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

	/* fk 값들의 이름을 가져오는 코드 */
	public List<ReservationDTO> setDTOField(List<ReservationEntity> reservationEntityList) {

		List<ReservationDTO> list =
			reservationEntityList.stream().map(reservationEntity -> mapper.map(reservationEntity, ReservationDTO.class))
				// 고객명
				.peek(reservationDTO -> reservationDTO.setCustomerName(
					mapper.map(customerRepository.findById(reservationDTO.getCustomerCodeFk()).orElse(null),
						CustomerDTO.class).getCustomerName()))
				// 객실명
				.peek(reservationDTO -> reservationDTO.setRoomName(String.valueOf(roomCategoryRepository.findById(
					roomRepository.findById(reservationDTO.getRoomCodeFk()).get().getRoomCategoryCodeFk()
				).get().getRoomName())))
				// 객실등급명
				.peek(reservationDTO -> reservationDTO.setRoomLevelName(
						roomLevelRepository.findById(
							roomCategoryRepository.findById(
								roomRepository.findById(
									reservationDTO.getRoomCodeFk()
								).get().getRoomCategoryCodeFk()
							).get().getRoomLevelCodeFk()
						).get().getRoomLevelName()
					)
				).toList();

		return list;
	}

	/* 투숙 다중 조건 검색을 위한 메소드 */
	// 지점, 객실코드, 체크인, 체크아웃
	// @Override
	// public List<Integer> selectStaysList(int pageNum, String branchCodeFk, String roomCodeFk,
	// 	LocalDateTime reservationCheckinDate, LocalDateTime reservationCheckoutDate) {
	//
	// 	Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
	// 	Specification<ReservationEntity> spec = (root, query, CriteriaBuilder) -> null;
	//
	// 	// 지점 코드
	// 	if (!branchCodeFk.isEmpty()) {
	// 		spec = spec.and(ReservationSpecification.equalsBranchCodeFk(branchCodeFk));
	// 	}
	// 	// 객실코드
	// 	if (!roomCodeFk.isEmpty()) {
	// 		spec = spec.and(ReservationSpecification.likeRoomCodeFk(roomCodeFk));
	// 	}
	// 	// 체크인날짜
	// 	if (reservationCheckinDate != null) {
	// 		spec = spec.and(ReservationSpecification.equalsCheckinDate(reservationCheckinDate));
	// 	}
	// 	// 체크아웃날짜
	// 	if (reservationCheckoutDate != null) {
	// 		spec = spec.and(ReservationSpecification.equalsCheckoutDate(reservationCheckoutDate));
	// 	}
	//
	// 	Page<ReservationEntity> reservationEntityPage = reservationRepository.findAll(spec, pageable);
	// 	// List<ReservationDTO> reservationDTOList = reservationEntityPage
	// 	// 	.stream()
	// 	// 	.map(reservationEntity -> mapper.map(reservationEntity, ReservationDTO.class))
	// 	// 	.toList();
	//
	// 	// Map<String, Object> roomPageInfo = new HashMap<>();
	//
	// 	// roomPageInfo.put(KEY_CONTENT, reservationDTOList);
	//
	// 	// 조회 결과에서 예약 코드만 리스트에 담아서 넘기기
	// 	List<Integer> reservationCodes = reservationEntityPage
	// 		.stream()
	// 		.map(ReservationEntity::getReservationCodePk)
	// 		.collect(Collectors.toList());
	//
	// 	return reservationCodes;
	// }
}
