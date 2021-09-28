package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class PchallanByBatchId {
    String pchallanRef;
    Long id;

    public PchallanByBatchId(String pchallanRef, Long count) {
        this.pchallanRef = pchallanRef;
        this.id = count;
    }
}
