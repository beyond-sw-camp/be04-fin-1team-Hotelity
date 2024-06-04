package org.iot.hotelitybackend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfig {

	@Value("${openai.api.key}")
	private String openAiKey;
	@Bean
	public RestTemplate template(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + openAiKey);
			return execution.execute(request, body);
		});
		return restTemplate;
	}
}
