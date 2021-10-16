package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.StockMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingBatchData {
    String batchId;
    String pchallanRef;
    Date receiveDate;
    Double totalBatchMtr;
    Double totalBatchWt;
    String qualityName;
    String qualityId;
    Long pcs;



    public PendingBatchData(String batchId, String pchallanRef, Double totalBatchMtr, Double totalBatchWt,Date receiveDate,String qualityName,String qualityId,Long pcs) {
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.totalBatchMtr = totalBatchMtr;
        this.totalBatchWt = totalBatchWt;
        this.receiveDate = receiveDate;
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.pcs = pcs;
    }
}
