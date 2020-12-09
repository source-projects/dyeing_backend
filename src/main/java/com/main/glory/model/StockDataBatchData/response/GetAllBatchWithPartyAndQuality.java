package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetAllBatchWithPartyAndQuality {
    String batchId;
    Long controlId;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;
    Long partyId;
    String partyName;
}
