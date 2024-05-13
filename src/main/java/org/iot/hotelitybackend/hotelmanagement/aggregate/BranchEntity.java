package org.iot.hotelitybackend.hotelmanagement.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "branch_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BranchEntity {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "branch_code_pk")
	private String branchCodePk;
	private String branchName;
	private String branchAddress;
	private String branchPhoneNumber;

	@Builder
	public BranchEntity(String branchCodePk, String branchName, String branchAddress, String branchPhoneNumber) {
		this.branchCodePk = branchCodePk;
		this.branchName = branchName;
		this.branchAddress = branchAddress;
		this.branchPhoneNumber = branchPhoneNumber;
	}
}
