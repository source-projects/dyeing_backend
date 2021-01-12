package com.main.glory.model.shade.requestmodals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetShadeByPartyAndQuality {

    Long id;
    String partyShadeNo;
    String colorTone;
    Long partyId;
    String partyCode;
    String partyName;
    Long qualityEntryId;
    String qualityId;
    String qualityName;



}
