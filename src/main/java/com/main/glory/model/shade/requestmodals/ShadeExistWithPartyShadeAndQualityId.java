package com.main.glory.model.shade.requestmodals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class ShadeExistWithPartyShadeAndQualityId {
    Long shadeId;
    String partyShadeNo;
    Long qualityEntryId;

    public ShadeExistWithPartyShadeAndQualityId(Long shadeId, String partyShadeNo, Long qualityEntryId) {
        this.shadeId = shadeId;
        this.partyShadeNo = partyShadeNo;
        this.qualityEntryId = qualityEntryId;
    }
}
