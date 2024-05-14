package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "notice_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeCodePk;
    private String noticeTitle;
    private String noticeContent;
    private Date noticePostedDate;
    private Integer employeeCodeFk;
	private Date noticeLastUpdatedDate;
	private String branchCodeFk;

	@Builder
	public NoticeEntity(
			Integer noticeCodePk,
			String noticeTitle,
			String noticeContent,
			Date noticePostedDate,
			Integer employeeCodeFk,
			Date noticeLastUpdatedDate,
			String branchCodeFk
	) {
		this.noticeCodePk = noticeCodePk;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.noticePostedDate = noticePostedDate;
		this.employeeCodeFk = employeeCodeFk;
		this.noticeLastUpdatedDate = noticeLastUpdatedDate;
		this.branchCodeFk = branchCodeFk;
	}
}
