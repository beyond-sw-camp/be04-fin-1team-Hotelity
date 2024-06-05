package org.iot.hotelitybackend.sales.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NoticeDashboardVO {
	private Integer noticeCodePk;
	private String noticeTitle;
	private Integer employeeCodeFk;
	private String PICEmployeeName;
	private LocalDateTime noticePostedDate;
}
