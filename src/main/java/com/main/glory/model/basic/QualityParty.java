package com.main.glory.model.basic;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityParty {
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;
    Long partyId;
    String partyName;

}
