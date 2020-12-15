package com.main.glory.model.program.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShadeIdwithPartyShadeNo {

    Long id;
    String qualityId;
    Long partyId;
    String partyShadeNo;
    String colorTone;

}
