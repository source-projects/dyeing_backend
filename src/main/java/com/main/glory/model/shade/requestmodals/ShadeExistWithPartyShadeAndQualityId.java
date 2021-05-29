package com.main.glory.model.shade.requestmodals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShadeExistWithPartyShadeAndQualityId {
    Long shadeId;
    String partyShadeNo;
    Long qualityEntryId;
}
