package org.iot.hotelitybackend.marketing.vo;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CampaignCustomerSearchCriteria {
	private Integer pageNum;
	private Integer campaignCodeFk;
	private String campaignSendType;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime campaignSentDate;

	private String customerName;
	private String campaignTitle;
	private Integer campaignSentStatus;
	private Integer templateCodeFk;
	private String templateName;
	private String orderBy;
	private Integer sortBy;
}
