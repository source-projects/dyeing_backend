package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class GetBatchWithControlId {
    String mergeBatchId;
    String batchId;
    Long controlId;
    Double WT;
    Double MTR;

    public GetBatchWithControlId(String batchId, Long controlId, Double WT, Double MTR) {
        this.batchId = batchId;
        this.controlId = controlId;
        this.WT = WT;
        this.MTR = MTR;
    }

    public GetBatchWithControlId(String mergeBatchId, String batchId, Long controlId, Double WT, Double MTR) {
        this.mergeBatchId = mergeBatchId;
        this.batchId = batchId;
        this.controlId = controlId;
        this.WT = WT;
        this.MTR = MTR;
    }
    public GetBatchWithControlId(String mergeBatchId,  Double WT, Double MTR) {
        this.mergeBatchId = mergeBatchId;
        this.batchId = batchId;
        this.controlId = controlId;
        this.WT = WT;
        this.MTR = MTR;
    }

    /*  public GetBatchWithControlId(String mergeBatchId, String batchId, Long controlId, Double WT, Double MTR) {
                this.mergeBatchId = mergeBatchId;
                this.batchId = batchId;
                this.controlId = controlId;
                this.WT = WT;
                this.MTR = MTR;
            }
        */
    public GetBatchWithControlId(GetBatchWithControlId getBatchWithControlIdData) {
        this.batchId=getBatchWithControlIdData.batchId;
        this.controlId=getBatchWithControlIdData.controlId;
        this.WT=getBatchWithControlIdData.WT;
        this.MTR=getBatchWithControlIdData.MTR;
    //    this.isProductionPlanned=getBatchWithControlIdData.isProductionPlanned;
    }

}
