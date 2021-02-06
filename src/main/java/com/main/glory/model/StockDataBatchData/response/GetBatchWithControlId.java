package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBatchWithControlId {
    String batchId;
    Long controlId;
    Double WT;
    Double MTR;


    public GetBatchWithControlId(GetBatchWithControlId getBatchWithControlIdData) {
        this.batchId=getBatchWithControlIdData.batchId;
        this.controlId=getBatchWithControlIdData.controlId;
        this.WT=getBatchWithControlIdData.WT;
        this.MTR=getBatchWithControlIdData.MTR;
    //    this.isProductionPlanned=getBatchWithControlIdData.isProductionPlanned;
    }

}
