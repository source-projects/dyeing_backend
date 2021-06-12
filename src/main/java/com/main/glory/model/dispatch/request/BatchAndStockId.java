package com.main.glory.model.dispatch.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BatchAndStockId {
    String batchId;
    Long stockId;
    Double rate;
    Long qualityEntryId;

    public BatchAndStockId(String batchId, Long stockId) {
        this.batchId = batchId;
        this.stockId = stockId;
    }
}
