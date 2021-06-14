package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class BatchWithTotalMTRandFinishMTR {
    String batchId;
    Long controlId;
    Double WT;
    Double MTR;
    Double totalFinishMtr;
    Long totalPcs;
    String qualityId;
    Double rate;
    String qualityName;
    Long qualityEntryId;


    public BatchWithTotalMTRandFinishMTR(String batchId, Long controlId, Double WT, Double MTR, Double totalFinishMtr, Long totalPcs) {
        this.batchId = batchId;
        this.controlId = controlId;
        this.WT = WT;
        this.MTR = MTR;
        this.totalFinishMtr = totalFinishMtr;
        this.totalPcs = totalPcs;
    }

    public BatchWithTotalMTRandFinishMTR(BatchWithTotalMTRandFinishMTR getBatchWithControlIdData) {
        this.batchId=getBatchWithControlIdData.batchId;
        this.controlId=getBatchWithControlIdData.controlId;
        this.WT=getBatchWithControlIdData.WT;
        this.MTR=getBatchWithControlIdData.MTR;
        this.totalFinishMtr=getBatchWithControlIdData.totalFinishMtr;
        this.totalPcs=getBatchWithControlIdData.totalPcs;
    }
}
