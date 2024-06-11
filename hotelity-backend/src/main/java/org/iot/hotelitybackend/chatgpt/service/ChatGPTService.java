package org.iot.hotelitybackend.chatgpt.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface ChatGPTService {
	String getDataOfToday(LocalDateTime now);

	Map<String, String> getReservationsDataOfToday(LocalDateTime now);

	Map<String, String> getPaymentsDataOfToday(LocalDateTime now);

	String getDailyChatGPTResponse(String promptDataString, String contentType);

	String getDataMonth(LocalDateTime now, String contentType);

	String getMonthlyChatGPTResponse(String promptDataString, String contentType);

	String getDataYear(LocalDateTime now, String contentType);

	String getYearlyChatGPTResponse(String promptDataString, String contentType);

	String getDailyData(LocalDateTime now, String contentType);
}
