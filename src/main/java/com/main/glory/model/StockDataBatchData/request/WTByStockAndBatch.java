package com.main.glory.model.StockDataBatchData.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class WTByStockAndBatch {
    String batchId;
    Long stockId;
    Double Totalwt;

    public WTByStockAndBatch(String batchId, Long stockId, Double totalwt) {
        this.batchId = batchId;
        this.stockId = stockId;
        this.Totalwt = totalwt;
    }
}
