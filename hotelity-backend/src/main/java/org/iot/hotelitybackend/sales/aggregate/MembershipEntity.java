package org.iot.hotelitybackend.sales.aggregate;

import java.util.List;
import java.util.Set;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "membership_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MembershipEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer membershipLevelCodePk;
	private String membershipLevelName;
	private String membershipInfo;
	private Integer membershipCriteriaAmount;

	@Builder
	public MembershipEntity(
		Integer membershipLevelCodePk,
		String membershipLevelName,
		String membershipInfo,
		Integer membershipCriteriaAmount
	) {
		this.membershipLevelCodePk = membershipLevelCodePk;
		this.membershipLevelName = membershipLevelName;
		this.membershipInfo = membershipInfo;
		this.membershipCriteriaAmount = membershipCriteriaAmount;
	}

	@OneToMany(mappedBy = "membership")
	private List<MembershipIssueEntity> membershipIssue;
}
