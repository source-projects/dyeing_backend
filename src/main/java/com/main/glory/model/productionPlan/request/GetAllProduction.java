package com.main.glory.model.productionPlan.request;

import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.response.GetQualityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllProduction extends GetAllProductionWithShadeData {

    String partyName;
    String qualityId;
    String qualityName;

    public GetAllProduction(GetAllProductionWithShadeData g, Party party, GetQualityResponse quality) {
        super(g);
        this.partyName=party.getPartyName();
        this.qualityId=quality.getQualityId();
        this.qualityName=quality.getQualityName();
    }
}
