package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "membership_issue_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MembershipIssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer membershipIssueCodePk;
    private Integer customerCodeFk;
    private Date membershipIssueDate;
    private Integer membershipLevelCodeFk;

	@Builder
	public MembershipIssueEntity(Integer membershipIssueCodePk, Integer customerCodeFk, Date membershipIssueDate,
		Integer membershipLevelCodeFk) {
		this.membershipIssueCodePk = membershipIssueCodePk;
		this.customerCodeFk = customerCodeFk;
		this.membershipIssueDate = membershipIssueDate;
		this.membershipLevelCodeFk = membershipLevelCodeFk;
	}
}
