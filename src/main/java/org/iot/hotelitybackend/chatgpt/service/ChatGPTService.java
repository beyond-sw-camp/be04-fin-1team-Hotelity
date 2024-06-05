package org.iot.hotelitybackend.chatgpt.service;

import java.time.LocalDateTime;

public interface ChatGPTService {
	String getDataOfToday(LocalDateTime now);

	String getDailyChatGPTResponse(String promptDataString);

	String getDataMonth(LocalDateTime now);

	String getMonthlyChatGPTResponse(String promptDataString);

	String getDataYear(LocalDateTime now);

	String getYearlyChatGPTResponse(String promptDataString);
}
