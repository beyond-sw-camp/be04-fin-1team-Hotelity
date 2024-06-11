package org.iot.hotelitybackend.sales.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NoticeSearchCriteria {
	private Integer pageNum;
	private Integer noticeCodePk;
	private String noticeTitle;
	private String noticeContent;
	private Integer employeeCodeFk;
	private String employeeName;
	private String branchCodeFk;
	private LocalDateTime noticePostedDate;
	private LocalDateTime noticeLastUpdatedDate;
	private String orderBy;
	private Integer sortBy;
}