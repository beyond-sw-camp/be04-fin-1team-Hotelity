package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.Formula;
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

	@Formula("("
		+ "SELECT e.employee_name "
		+ "FROM notice_tb n "
		+ "JOIN employee_tb e ON n.employee_code_fk = e.employee_code_pk "
		+ "WHERE n.notice_code_pk = notice_code_pk "
		+ ")")
	private String picemployeeName;

	@Builder
	public NoticeEntity(
		Integer noticeCodePk,
		String noticeTitle,
		String noticeContent,
		Integer employeeCodeFk,
		String branchCodeFk,
		LocalDateTime noticePostedDate,
		LocalDateTime noticeLastUpdatedDate,
		String picemployeeName
	) {
		this.noticeCodePk = noticeCodePk;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.noticePostedDate = noticePostedDate;
		this.employeeCodeFk = employeeCodeFk;
		this.noticeLastUpdatedDate = noticeLastUpdatedDate;
		this.branchCodeFk = branchCodeFk;
		this.picemployeeName = picemployeeName;
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
