package org.iot.hotelitybackend.hotelmanagement.aggregate;

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
@Table(name = "ancillary_image_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AncillaryImageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ancillary_image_code_pk")
	private Integer ancillaryImageCodePk;
	private Integer ancillaryCodeFk;
	private String ancillaryImageLink;
}
