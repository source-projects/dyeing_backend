package com.main.glory.model.StockDataBatchData.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockFilter {
    String partyCode;
    Long partyId;
    Long qualityEntryId;
}
