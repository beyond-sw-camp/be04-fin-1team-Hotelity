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
@Table(name = "rank_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rank_code_pk")
	private Integer rankCodePk;
	private String rankName;

	@Builder
	public RankEntity(Integer rankCodePk, String rankName) {
		this.rankCodePk = rankCodePk;
		this.rankName = rankName;
	}
}
