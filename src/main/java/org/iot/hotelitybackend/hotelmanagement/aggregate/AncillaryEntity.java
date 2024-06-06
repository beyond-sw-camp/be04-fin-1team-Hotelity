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
import org.hibernate.annotations.Formula;

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
	private String branchCodeFk;

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

	@Formula(
		"(" +
			"SELECT b.branch_name " +
			"FROM ancillary_tb a " +
			"JOIN branch_tb b ON a.branch_code_fk = b.branch_code_pk " +
			"WHERE a.ancillary_code_pk = ancillary_code_pk " +
			")"
	)
	private String branchName;

	@Formula(
			"(" +
					"SELECT ac.ancillary_category_name " +
					"FROM ancillary_tb a " +
					"JOIN ancillary_category_tb ac ON a.ancillary_category_code_fk = ac.ancillary_category_code_pk " +
					"WHERE a.ancillary_code_pk = ancillary_code_pk " +
					")"
	)
	private String ancillaryCategoryName;

	@Builder
	public AncillaryEntity(
            Integer ancillaryCodePk,
            String ancillaryName,
            String branchCodeFk,
            String ancillaryLocation,
            LocalTime ancillaryOpenTime,
            LocalTime ancillaryCloseTime,
            String ancillaryPhoneNumber,
            Integer ancillaryCategoryCodeFk,
            String branchName,
			String ancillaryCategoryName
    ) {
		this.ancillaryCodePk = ancillaryCodePk;
		this.ancillaryName = ancillaryName;
		this.branchCodeFk = branchCodeFk;
		this.ancillaryLocation = ancillaryLocation;
		this.ancillaryOpenTime = ancillaryOpenTime;
		this.ancillaryCloseTime = ancillaryCloseTime;
		this.ancillaryPhoneNumber = ancillaryPhoneNumber;
		this.ancillaryCategoryCodeFk = ancillaryCategoryCodeFk;
        this.branchName = branchName;
        this.ancillaryCategoryName = ancillaryCategoryName;
    }
}
