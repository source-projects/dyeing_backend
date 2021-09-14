package com.main.glory.model.quality;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QualityWithPartyName extends Quality {
	String partyCode;
	private String partyName;
	String userHeadName;

	public QualityWithPartyName(Quality other, String partyName,String partyCode) {
		super(other);
		this.partyName = partyName;
		this.partyCode=partyCode;
	}
	public QualityWithPartyName(Quality other, String partyName,String partyCode,String userHeadName) {
		super(other);
		this.partyName = partyName;
		this.partyCode=partyCode;
		this.userHeadName = userHeadName;
	}
}
