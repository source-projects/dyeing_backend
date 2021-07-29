package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class GetAllBatchResponse {
    Double totalMtr;
    Double totalWt;
    String batchId;
    String qualityName;

    public GetAllBatchResponse(Double totalMtr, Double totalWt, String batchId) {
        this.totalMtr = totalMtr;
        this.totalWt = totalWt;
        this.batchId = batchId;
    }

    public GetAllBatchResponse(Double totalMtr, Double totalWt, String batchId, String qualityName) {
        this.totalMtr = totalMtr;
        this.totalWt = totalWt;
        this.batchId = batchId;
        this.qualityName = qualityName;
    }
}
