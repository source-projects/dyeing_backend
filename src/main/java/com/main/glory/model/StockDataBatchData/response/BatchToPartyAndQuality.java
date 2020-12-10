package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchToPartyAndQuality {
    Long partyId;
    String partyName;
    Long qualityEntryId;
    String qualityId;
    String qualityName;

}
