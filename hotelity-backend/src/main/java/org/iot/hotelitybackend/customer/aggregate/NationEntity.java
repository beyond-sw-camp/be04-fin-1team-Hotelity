package org.iot.hotelitybackend.customer.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nationality_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nation_code_pk")
	public Integer nationCodePk;
	public String nationName;

	@Builder
	public NationEntity(Integer nationCodePk, String nationName) {
		this.nationCodePk = nationCodePk;
		this.nationName = nationName;
	}
}
