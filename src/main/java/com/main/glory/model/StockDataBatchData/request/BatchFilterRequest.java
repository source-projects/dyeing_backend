package com.main.glory.model.StockDataBatchData.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchFilterRequest {
    String from;
    String to;
    Long partyId;
    Long qualityEntryId;
    Long qualityNameId;
    Long userHeadId;
}
