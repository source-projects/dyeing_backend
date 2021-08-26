package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.StockMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class GetAllStockWithoutBatches {
    Long stockId;
    Long partyId;
    Long qualityEntryId;

    public GetAllStockWithoutBatches(StockMast stockMast) {
        this.partyId=stockMast.getParty().getId();
        this.qualityEntryId=stockMast.getQuality().getId();
        this.stockId=stockMast.getId();
    }
}
