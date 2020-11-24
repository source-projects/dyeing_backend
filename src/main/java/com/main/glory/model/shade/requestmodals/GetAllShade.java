package com.main.glory.model.shade.requestmodals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetAllShade {

    Long id;
    String partyShadeNo;
    Long processId;
    String processName;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String partyName;
    Long partyId;
    String colorTone;


}
