package org.iot.hotelitybackend.hotelservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.iot.hotelitybackend.customer.repository.CustomerRepository;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomCategoryEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomLevelEntity;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomCategoryRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomLevelRepository;
import org.iot.hotelitybackend.hotelmanagement.repository.RoomRepository;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.dto.ReservationDTO;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional            // 테스트 후 rollback 처리를 위한 어노테이션
@SpringBootTest            // 통합 테스트 환경을 위해 스프링 컨텍스트에 등록된 Bean을 가져옴
@AutoConfigureMockMvc    // 실제 서버에 배포하지 않고 테스트하기 위한 MockMvc 환경을 자동 구성
class ReservationServiceImplTest {

	// 테스트할 서비스를 생성자 주입 받음
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ModelMapper mapper;

	// 해당 서비스의 메소드를 실행하기 위해 필요한 컴포넌트들을 MockBean으로 등록
	@MockBean
	ReservationRepository reservationRepository;
	@MockBean
	CustomerRepository customerRepository;
	@MockBean
	RoomRepository roomRepository;
	@MockBean
	RoomCategoryRepository roomCategoryRepository;
	@MockBean
	RoomLevelRepository roomLevelRepository;

	// 테스트 코드 실행 전 먼저 동작하는 코드
	@BeforeEach
	public void setup() {
		ReservationEntity reservationEntity = ReservationEntity.builder()
			.reservationCheckinDate(LocalDateTime.of(2023, 5, 5, 0, 0))
			.build();

		when(reservationRepository.findByReservationCheckinDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
			.thenReturn(List.of(reservationEntity));

		CustomerEntity customerEntity = CustomerEntity.builder()
			.customerCodePk(1)
			.customerName("TestName")
			.build();

		when(customerRepository.findById(any(Integer.class)))
			.thenReturn(java.util.Optional.of(customerEntity));

		RoomEntity roomEntity = RoomEntity.builder()
			.roomCategoryCodeFk(1)
			.build();

		when(roomRepository.findById(any(String.class)))
			.thenReturn(java.util.Optional.of(roomEntity));

		RoomCategoryEntity roomCategoryEntity = RoomCategoryEntity.builder()
			.roomName("TestRoomName")
			.roomLevelCodeFk(1)
			.build();

		when(roomCategoryRepository.findById(any(Integer.class)))
			.thenReturn(java.util.Optional.of(roomCategoryEntity));

		RoomLevelEntity roomLevelEntity = RoomLevelEntity.builder()
			.roomLevelName("TestRoomLevelName")
			.build();

		when(roomLevelRepository.findById(any(Integer.class)))
			.thenReturn(java.util.Optional.of(roomLevelEntity));
	}

	@DisplayName("월별 예약 내역 리스트 전체 조회 테스트")
	@Test
	public void selectReservationListByMonthTest() throws Exception {
		// given
		int year = 2024;
		int month = 5;

		// when
		// Map<String, Object> result = reservationService.selectReservationListByMonth(year, month);

		// then
		// assertThat(result).isNotNull();							// 메소드 수행 결과가 null이 아닌지 검증
		// assertThat(result.containsKey("content")).isTrue();
		//
		// List<ReservationDTO> reservationDTOList = (List<ReservationDTO>) result.get("content");
		// assertThat(reservationDTOList).isNotEmpty();
		//
		// ReservationDTO reservationDTO = reservationDTOList.get(0);
		// assertThat(reservationDTO.getCustomerName()).isEqualTo("TestName");
		// assertThat(reservationDTO.getRoomName()).isEqualTo("TestRoomName");
		// assertThat(reservationDTO.getRoomLevelName()).isEqualTo("TestRoomLevelName");
	}


	/* 일자별 예약 리스트 조회 */

	/* 예약 코드로 검색 */

}