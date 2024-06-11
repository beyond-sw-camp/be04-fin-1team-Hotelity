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
@Table(name = "ancillary_category_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AncillaryCategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ancillary_category_code_pk")
	private Integer ancillaryCategoryCodePk;

	@Column(name = "ancillaryCategoryName")
	private String ancillaryCategoryName;

	@Builder
	public AncillaryCategoryEntity(Integer ancillaryCategoryCodePk, String ancillaryCategoryName) {
		this.ancillaryCategoryCodePk = ancillaryCategoryCodePk;
		this.ancillaryCategoryName = ancillaryCategoryName;
	}
}
