package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
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
    String pchallanRef;
    @JsonIgnore
    StockMast stockMast;


    public BatchWithTotalMTRandFinishMTR(String batchId, Long controlId, Double WT, Double MTR, Double totalFinishMtr, Long totalPcs) {
        this.batchId = batchId;
        this.controlId = controlId;
        this.WT = StockBatchServiceImpl.changeInFormattedDecimal(WT);
        this.MTR = StockBatchServiceImpl.changeInFormattedDecimal(MTR);
        this.totalFinishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.totalPcs = totalPcs;
    }

    //query issue in function
    //getAllPChallanByPartyIdAndRfInvoiceFlagWithTotalFinishMtr
    public BatchWithTotalMTRandFinishMTR(String batchId, String pchallanRef,Long controlId, Double WT, Double MTR, Double totalFinishMtr, Long totalPcs,StockMast sm) {
        this.batchId = batchId;
        this.controlId = controlId;
        this.pchallanRef = pchallanRef;
        this.WT = StockBatchServiceImpl.changeInFormattedDecimal(WT);
        this.MTR = StockBatchServiceImpl.changeInFormattedDecimal(MTR);
        this.totalFinishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.totalPcs = totalPcs;
        this.stockMast = sm;
    }

    public BatchWithTotalMTRandFinishMTR(Long controlId, Double WT, Double MTR, Double totalFinishMtr, Long totalPcs,String pchallanRef,String batchId) {
        this.controlId = controlId;
        this.WT = WT;
        this.MTR = MTR;
        this.totalFinishMtr = totalFinishMtr;
        this.totalPcs = totalPcs;
        this.pchallanRef = pchallanRef;
        this.batchId = batchId;//group by batch and pchallanRef
    }





    public BatchWithTotalMTRandFinishMTR(BatchWithTotalMTRandFinishMTR getBatchWithControlIdData) {
        this.batchId=getBatchWithControlIdData.batchId==null?null:getBatchWithControlIdData.getBatchId();
        this.controlId=getBatchWithControlIdData.controlId;
        this.WT=StockBatchServiceImpl.changeInFormattedDecimal(getBatchWithControlIdData.WT);
        this.MTR= StockBatchServiceImpl.changeInFormattedDecimal(getBatchWithControlIdData.MTR);
        this.totalFinishMtr=StockBatchServiceImpl.changeInFormattedDecimal(getBatchWithControlIdData.totalFinishMtr);
        this.totalPcs=getBatchWithControlIdData.totalPcs;
        this.pchallanRef = getBatchWithControlIdData.pchallanRef;
    }
}
