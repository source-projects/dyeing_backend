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


    public PendingBatchData(StockMast stockMast, GetBatchWithControlId batch) {
        this.batchId = batch.getBatchId();
        this.receiveDate = stockMast.getReceiveDate();
        this.totalBatchMtr = batch.getMTR();
        this.totalBatchWt = batch.getWT();
    }
}
