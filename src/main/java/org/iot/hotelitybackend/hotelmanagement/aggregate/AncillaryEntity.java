package org.iot.hotelitybackend.hotelmanagement.aggregate;

import java.time.LocalTime;

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
@Table(name = "ancillary_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AncillaryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ancillary_code_pk")
	private Integer ancillaryCodePk;

	@Column(name = "ancillary_name")
	private String ancillaryName;

	@Column(name = "branch_code_fk")
	private Integer branchCodeFk;

	@Column(name = "ancillary_location")
	private String ancillaryLocation;

	@Column(name = "ancillary_open_time")
	private LocalTime ancillaryOpenTime;

	@Column(name = "ancillary_close_time")
	private LocalTime ancillaryCloseTime;

	@Column(name = "ancillary_phone_number")
	private String ancillaryPhoneNumber;

	@Column(name = "ancillary_category_code_fk")
	private Integer ancillaryCategoryCodeFk;

	@Builder
	public AncillaryEntity(Integer ancillaryCodePk, String ancillaryName, Integer branchCodeFk,
		String ancillaryLocation,
		LocalTime ancillaryOpenTime, LocalTime ancillaryCloseTime, String ancillaryPhoneNumber,
		Integer ancillaryCategoryCodeFk) {
		this.ancillaryCodePk = ancillaryCodePk;
		this.ancillaryName = ancillaryName;
		this.branchCodeFk = branchCodeFk;
		this.ancillaryLocation = ancillaryLocation;
		this.ancillaryOpenTime = ancillaryOpenTime;
		this.ancillaryCloseTime = ancillaryCloseTime;
		this.ancillaryPhoneNumber = ancillaryPhoneNumber;
		this.ancillaryCategoryCodeFk = ancillaryCategoryCodeFk;
	}
}
