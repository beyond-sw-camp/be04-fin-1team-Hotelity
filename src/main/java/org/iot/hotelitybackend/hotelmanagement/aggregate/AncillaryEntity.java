package org.iot.hotelitybackend.hotelmanagement.aggregate;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
	public Integer ancillaryCodePk;

	@Column(name = "ancillary_name")
	public String ancillaryName;

	@Column(name = "branch_code_fk")
	public Integer branchCodeFk;

	@Column(name = "ancillary_location")
	public String ancillaryLocation;

	@Column(name = "ancillary_open_time")
	public LocalTime ancillaryOpenTime;

	@Column(name = "ancillary_close_time")
	public LocalTime ancillaryCloseTime;

	@Column(name = "ancillary_phone_number")
	public String ancillaryPhoneNumber;

	@Column(name = "ancillary_category_code_fk")
	public Integer ancillaryCategoryCodeFk;
}
