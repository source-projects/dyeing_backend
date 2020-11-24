package com.main.glory.model.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartyQuality {



    Long partyId;
    String partyName;
    List<QualityData> qualityDataList;

}
