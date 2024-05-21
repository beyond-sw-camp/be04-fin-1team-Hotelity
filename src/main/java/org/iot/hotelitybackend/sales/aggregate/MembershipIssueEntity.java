package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
//
// @Entity
// @Table(name = "membership_issue_tb")
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @Getter
// public class MembershipIssueEntity {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer membershipIssueCodePk;
//     private Integer customerCodeFk;
//     private Date membershipIssueDate;
//
// 	@Column(name = "membership_level_code_fk", insertable = false, updatable = false)
//     private Integer membershipLevelCodeFk;
//
// 	@Builder
// 	public MembershipIssueEntity(
// 			Integer membershipIssueCodePk,
// 			Integer customerCodeFk,
// 			Date membershipIssueDate,
// 			Integer membershipLevelCodeFk
// 	) {
// 		this.membershipIssueCodePk = membershipIssueCodePk;
// 		this.customerCodeFk = customerCodeFk;
// 		this.membershipIssueDate = membershipIssueDate;
// 		this.membershipLevelCodeFk = membershipLevelCodeFk;
// 	}
//
// 	@ManyToOne(fetch = FetchType.LAZY)
// 	@JoinColumn(name = "customer_code_fk", referencedColumnName = "customer_code_pk")
// 	private CustomerEntity customer;
//
// 	@ManyToOne
// 	@JoinColumn(name = "membership_level_code_fk")  // 이 필드의 외래 키 이름을 지정합니다.
// 	private MembershipEntity membership;
// }

@Entity
@Table(name = "membership_issue_tb")
@NoArgsConstructor
@Getter
@Setter
public class MembershipIssueEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer membershipIssueCodePk;

	@Column(name = "customer_code_fk")
	private Integer customerCodeFk;
	private Date membershipIssueDate;
	private Integer membershipLevelCodeFk;

	@Builder
	public MembershipIssueEntity(
			Integer membershipIssueCodePk,
			Integer customerCodeFk,
			Date membershipIssueDate,
			Integer membershipLevelCodeFk
	) {
		this.membershipIssueCodePk = membershipIssueCodePk;
		this.customerCodeFk = customerCodeFk;
		this.membershipIssueDate = membershipIssueDate;
		this.membershipLevelCodeFk = membershipLevelCodeFk;
	}

	@ManyToOne
	@JoinColumn(name = "customer_code_fk", insertable = false, updatable = false)
	private CustomerEntity customer;
}
