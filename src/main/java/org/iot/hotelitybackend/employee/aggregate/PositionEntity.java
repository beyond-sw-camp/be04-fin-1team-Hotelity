package org.iot.hotelitybackend.employee.aggregate;

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
@Table(name = "position_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PositionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "position_code_pk")
	private Integer positionCodePk;
	private String positionName;

	@Builder
	public PositionEntity(Integer positionCodePk, String positionName) {
		this.positionCodePk = positionCodePk;
		this.positionName = positionName;
	}
}
