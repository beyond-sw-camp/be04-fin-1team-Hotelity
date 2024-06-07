package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;

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

	@Column(name = "employee_code_fk")
	private Integer employeeCodeFk;

	private String branchCodeFk;
	private LocalDateTime noticePostedDate;
	private LocalDateTime noticeLastUpdatedDate;

	@Builder
	public NoticeEntity(
		Integer noticeCodePk,
		String noticeTitle,
		String noticeContent,
		Integer employeeCodeFk,
		String branchCodeFk,
		LocalDateTime noticePostedDate,
		LocalDateTime noticeLastUpdatedDate
	) {
		this.noticeCodePk = noticeCodePk;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.noticePostedDate = noticePostedDate;
		this.employeeCodeFk = employeeCodeFk;
		this.noticeLastUpdatedDate = noticeLastUpdatedDate;
		this.branchCodeFk = branchCodeFk;
	}

	@Override
	public String toString() {
		return "NoticeEntity{" +
				"noticeCodePk=" + noticeCodePk +
				", noticeTitle='" + noticeTitle + '\'' +
				", noticeContent='" + noticeContent + '\'' +
				", employeeCodeFk=" + employeeCodeFk +
				", branchCodeFk='" + branchCodeFk + '\'' +
				", noticePostedDate=" + noticePostedDate +
				", noticeLastUpdatedDate=" + noticeLastUpdatedDate +
				'}';
	}

	@ManyToOne
	@JoinColumn(name = "employee_code_fk", insertable = false, updatable = false)
	private EmployeeEntity employee;
}
