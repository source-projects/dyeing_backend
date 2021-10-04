package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllPendingShade extends ShadeMast {

    String partyName;
    String qualityId;
    String qualityType;
    String qualityName;

    public GetAllPendingShade(ShadeMast s, Party party, Quality quality) {
        super(s);
        this.partyName=party.getPartyName();
        this.qualityId=quality.getQualityId();
        this.qualityName=quality.getQualityName().getQualityName();
        this.qualityType=quality.getQualityType();

    }
}
