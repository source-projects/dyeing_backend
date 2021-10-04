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


    public PendingBatchData(String batchId, String pchallanRef, Double totalBatchMtr, Double totalBatchWt,Date receiveDate) {
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.totalBatchMtr = totalBatchMtr;
        this.totalBatchWt = totalBatchWt;
        this.receiveDate = receiveDate;

    }
}
