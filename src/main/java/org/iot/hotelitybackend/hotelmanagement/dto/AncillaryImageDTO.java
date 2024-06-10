package org.iot.hotelitybackend.hotelmanagement.dto;

import lombok.Data;

@Data
public class AncillaryImageDTO {
	private Integer ancillaryImageCodePk;
	private Integer ancillaryCodeFk;
	private String ancillaryImageLink;
}
