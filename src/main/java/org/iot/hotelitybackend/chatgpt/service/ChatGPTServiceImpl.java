package org.iot.hotelitybackend.chatgpt.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iot.hotelitybackend.chatgpt.dto.ChatGPTRequest;
import org.iot.hotelitybackend.chatgpt.dto.ChatGPTResponse;
import org.iot.hotelitybackend.hotelservice.aggregate.PaymentEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.ReservationEntity;
import org.iot.hotelitybackend.hotelservice.aggregate.StayEntity;
import org.iot.hotelitybackend.hotelservice.repository.PaymentRepository;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.iot.hotelitybackend.hotelservice.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGPTServiceImpl implements ChatGPTService{

	@Value("${openai.model}")
	private String model;

	@Value("${openai.api.url}")
	private String apiURL;

	private RestTemplate template;
	private final ReservationRepository reservationRepository;
	private final StayRepository stayRepository;
	private final PaymentRepository paymentRepository;

	@Autowired
	public ChatGPTServiceImpl(RestTemplate template, ReservationRepository reservationRepository, StayRepository stayRepository,
		PaymentRepository paymentRepository) {
		this.template = template;
		this.reservationRepository = reservationRepository;
		this.stayRepository = stayRepository;
		this.paymentRepository = paymentRepository;
	}

	@Override
	public String getDataOfToday(LocalDateTime now) {
		StringBuilder reservationListData = new StringBuilder();
		StringBuilder stayListData = new StringBuilder();

		List<ReservationEntity> reservationEntityList = reservationRepository.findByReservationCheckinDate(now);
		for (ReservationEntity reservationEntity : reservationEntityList) {
			reservationListData.append(reservationEntity.toString()).append("\n");
		}
		System.out.println("reservationListData = " + reservationListData);

		List<StayEntity> stayEntityList = stayRepository.findByStayCheckinTime(now);
		for (StayEntity stayEntity : stayEntityList) {
			stayListData.append(stayEntity.toString()).append("\n");
		}
		System.out.println("stayListData = " + stayListData);

		String data =
			"금일 예약 개수: " + reservationEntityList.size() + ", 금일 투숙 개수: " + stayEntityList.size()
			+ " \n예약: " + reservationListData + ", 투숙: " + stayListData;
		return data;
	}

	@Override
	public Map<String, String> getReservationsDataOfToday(LocalDateTime now) {
		StringBuilder reservationListData = new StringBuilder();
		List<ReservationEntity> reservationEntityList = reservationRepository.findByReservationCheckinDate(now);
		for (ReservationEntity reservationEntity : reservationEntityList) {
			reservationListData.append(reservationEntity.toString()).append("\n");
		}
		System.out.println("Daily reservationListData = " + reservationListData);
		Map<String, String> promptDataString = new HashMap<>();
		promptDataString.put("reservationListData", reservationListData.toString());
		promptDataString.put("contentType", "예약");
		return promptDataString;
	}

	@Override
	public Map<String, String> getPaymentsDataOfToday(LocalDateTime now) {
		StringBuilder paymentListData = new StringBuilder();

		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		LocalDateTime startOfDay =
				LocalDateTime.of(year, month, day, 0, 0, 0);
		LocalDateTime endOfDay =
				LocalDateTime.of(year, month, day, 23, 59, 59);

		// 해당일자의 월별 결제 데이터 수집
		List<PaymentEntity> paymentEntityList =
				paymentRepository.findAllByPaymentDateBetween(
						java.sql.Timestamp.valueOf(startOfDay),
						java.sql.Timestamp.valueOf(endOfDay)
				);
		if (!paymentEntityList.isEmpty()) {
			for (PaymentEntity paymentEntity : paymentEntityList) {
				paymentListData.append(paymentEntity.toString()).append("\n");
			}
		} else {
			paymentListData.append("결제 데이터가 없습니다.");
		}
		System.out.println("paymentListData = " + paymentListData);
		Map<String, String> promptDataString = new HashMap<>();
		promptDataString.put("paymentListData", paymentListData.toString());
		promptDataString.put("contentType", "결제");
		return promptDataString;
	}

	@Override
	public String getDailyChatGPTResponse(String promptDataString, String contentType) {
		String prompt;
		if (contentType.equals("예약")) {
			prompt = "금일 예약과 투숙 정보를 아래의 양식에 맞게 150자 내외로 요약하여 설명해. \n"
				+ "금일 체크인 예정인 예약은 ~건이며, 현재 체크인 투숙이 진행된 예약은 ~건입니다. \n"
				+ "그리고 금일 예약 객실 관련 특이사항으로는 ~가 있습니다. \n";
		} else {
			prompt = "금일 결제 정보를 아래의 양식에 맞게 150자 내외로 요약하여 설명해. \n"
				+ "금일 결제 건수는 ~건이며, 결제 금액 총합은 ~원입니다. \n"
				+ "그리고 금일 결제 관련 특이사항으로는 ~가 있습니다. \n";
		}

		ChatGPTRequest request = new ChatGPTRequest(model, prompt);
		ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);

		if (chatGPTResponse != null) {
			return chatGPTResponse.getChoices().get(0).getMessage().getContent();
		}
		return null;
	}

	@Override
	public String getDataMonth(LocalDateTime now, String contentType) {

		String thisMonthData = getMonthlyDataString(now, contentType);
		String lastMonthData = getMonthlyDataString(now.minusMonths(1), contentType);

		String data =
			"이번달 " + contentType + " 데이터: \n" + thisMonthData + " \n" +
				"지난달 " + contentType + "데이터: \n" + lastMonthData + " \n";

		return data;
	}

	private String getMonthlyDataString(LocalDateTime now, String contentType) {
		StringBuilder paymentListData = new StringBuilder();
		StringBuilder reservationListData = new StringBuilder();

		int year = now.getYear();
		int month = now.getMonthValue();
		LocalDateTime startOfMonth =
			LocalDateTime.of(year, month, 1, 0, 0, 0);
		LocalDateTime endOfMonth =
			LocalDateTime.of(year, month, startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear()),
				23, 59, 59);

		String data;
		if (contentType.equals("결제")) {
			// 해당일자의 월별 결제 데이터 수집
			List<PaymentEntity> paymentEntityList =
				paymentRepository.findAllByPaymentDateBetween(
					java.sql.Timestamp.valueOf(startOfMonth),
					java.sql.Timestamp.valueOf(endOfMonth)
				);
			if (!paymentEntityList.isEmpty()) {
				for (PaymentEntity paymentEntity : paymentEntityList) {
					paymentListData.append(paymentEntity.toString()).append("\n");
				}
			} else {
				paymentListData.append("결제 데이터가 없습니다.");
			}
			System.out.println("paymentListData = " + paymentListData);
			data =
				year + "년" + month + "월 결제: " + paymentEntityList.size() + "건, " +
					year + "년" + month + "월 결제내용: " + paymentListData + " \n ";
		} else {
			// 해당일자의 월별 예약 데이터 수집
			List<ReservationEntity> reservationEntityList =
				reservationRepository.findByReservationCheckinDateBetween(startOfMonth, endOfMonth);
			if (!reservationEntityList.isEmpty()) {
				for (ReservationEntity reservationEntity : reservationEntityList) {
					reservationListData.append(reservationEntity.toString()).append("\n");
				}
			} else {
				reservationListData.append("예약 데이터가 없습니다.");
			}
			System.out.println("reservationListData = " + reservationListData);
			data =
				year + "년" + month + "월 예약: " + reservationEntityList.size() + "건, " +
					year + "년" + month + "월 예약내용: " + reservationListData + " \n ";
		}

		return data;
	}


	@Override
	public String getMonthlyChatGPTResponse(String promptDataString, String contentType) {
		String prompt;
		if (contentType.equals("예약")) {
			prompt = promptDataString + " \n"
				+ "이러한 이번달과 지난달의 예약 정보를 아래의 양식에 맞게 150자 내외로 요약하여 설명해. \n"
				+ "지난달 예약 건수: ~건, 지난달 예약 건수: ~건\n"
				+ "지난달에 비해 이번달 예약 건수 총합은 ~% 증가/감소 했습니다. \n"
				+ "지난달과 이번달 예약 내용 중 특이사항으로는 ~가 있습니다. \n";
		} else {
			prompt = promptDataString + " \n"
				+ "이러한 이번달과 지난달의 결제 정보를 아래의 양식에 맞게 150자 내외로 요약하여 설명해. \n"
				+ "지난달 결제 건수: ~건, 결제 금액 총합: ~원 \n"
				+ "이번달 결제 건수: ~건, 결제 금액 총합: ~원 \n"
				+ "지난달에 비해 이번달 결제 금액 총합은 ~% 증가/감소 했습니다. \n"
				+ "지난달과 이번달 결제 내용 중 특이사항으로는 ~가 있습니다. \n";
		}

		ChatGPTRequest request = new ChatGPTRequest(model, prompt);
		ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);

		if (chatGPTResponse != null) {
			return chatGPTResponse.getChoices().get(0).getMessage().getContent();
		}
		return null;
	}

	@Override
	public String getDataYear(LocalDateTime now, String contentType) {
		String thisYearData = getYearlyDataString(now, contentType);
		String lastYearData = getYearlyDataString(now.minusYears(1), contentType);

		String data =
			"올해 " + contentType + " 데이터: \n" + thisYearData + " \n" +
				"작년 " + contentType + " 데이터: \n" + lastYearData + " \n";
		return data;
	}

	private String getYearlyDataString(LocalDateTime now, String contentType) {
		StringBuilder paymentListData = new StringBuilder();
		StringBuilder reservationListData = new StringBuilder();

		int year = now.getYear();
		LocalDateTime startOfYear =
			LocalDateTime.of(year, 1, 1, 0, 0, 0);
		LocalDateTime endOfYear =
			LocalDateTime.of(year, 12, startOfYear.getMonth().length(startOfYear.toLocalDate().isLeapYear()),
				23, 59, 59);

		String data;
		if (contentType.equals("결제")) {
			// 해당일자의 연별 결제 데이터 수집
			List<PaymentEntity> paymentEntityList =
				paymentRepository.findAllByPaymentDateBetween(
					java.sql.Timestamp.valueOf(startOfYear),
					java.sql.Timestamp.valueOf(endOfYear)
				);
			if (!paymentEntityList.isEmpty()) {
				for (PaymentEntity paymentEntity : paymentEntityList) {
					paymentListData.append(paymentEntity.toString()).append("\n");
				}
			} else {
				paymentListData.append("결제 데이터가 없습니다.");
			}
			System.out.println("paymentListData = " + paymentListData);
			data =
				year + "년 결제: " + paymentEntityList.size() + "건, " +
					year + "년 결제내용: " + paymentListData + " \n ";
		} else {
			// 해당일자의 연별 예약 데이터 수집
			List<ReservationEntity> reservationEntityList =
				reservationRepository.findByReservationCheckinDateBetween(startOfYear, endOfYear);
			if (!reservationEntityList.isEmpty()) {
				for (ReservationEntity reservationEntity : reservationEntityList) {
					reservationListData.append(reservationEntity.toString()).append("\n");
				}
			} else {
				reservationListData.append("예약 데이터가 없습니다.");
			}
			System.out.println("reservationListData = " + reservationListData);
			data =
				year + "년 예약: " + reservationEntityList.size() + "건, " +
					year + "년 예약내용: " + reservationListData + " \n ";
		}

		return data;
	}

	@Override
	public String getYearlyChatGPTResponse(String promptDataString, String contentType) {

		String prompt;
		if (contentType.equals("예약")) {
			prompt = promptDataString + " \n"
				+ "이러한 올해와 지난해의 예약 정보를 아래의 양식에 맞게 150자 내외로 요약하여 설명해. \n"
				+ "작년 예약 건수: ~건, 올해 예약 건수: ~건 \n"
				+ "작년에 비해 올해 예약 건수 총합은 ~% 증가/감소 했습니다. \n"
				+ "작년과 올해 예약 내용 중 특이사항으로는 ~가 있습니다. \n";
		} else {
			prompt = promptDataString + " \n"
				+ "이러한 올해와 지난해의 결제 정보를 아래의 양식에 맞게 150자 내외로 요약하여 설명해. \n"
				+ "작년 결제 건수: ~건, 결제 금액 총합: ~원 \n"
				+ "올해 결제 건수: ~건, 결제 금액 총합: ~원 \n"
				+ "작년에 비해 올해 결제 금액 총합은 ~% 증가/감소 했습니다. \n"
				+ "작년과 올해 결제 내용 중 특이사항으로는 ~가 있습니다. \n";
		}

		ChatGPTRequest request = new ChatGPTRequest(model, prompt);
		ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);

		if (chatGPTResponse != null) {
			return chatGPTResponse.getChoices().get(0).getMessage().getContent();
		}
		return null;
	}
}
