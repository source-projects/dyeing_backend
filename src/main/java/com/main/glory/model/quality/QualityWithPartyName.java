package com.main.glory.model.quality;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QualityWithPartyName extends Quality {
	private String  partyName;

	public QualityWithPartyName(Quality other, String partyName) {
		super(other);
		this.partyName = partyName;
	}
}
