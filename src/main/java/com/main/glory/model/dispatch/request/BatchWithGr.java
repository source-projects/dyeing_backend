package com.main.glory.model.dispatch.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchWithGr {

    String pchallanRef;
    String batchId;
    Long controlId;
    List<BatchData> batchDataList;


    public BatchWithGr(GetBatchByInvoice batch) {
        this.batchId=batch.getBatchId();
        this.controlId=batch.getStockId();
    }

    public BatchWithGr(String batchId,Long controlId) {
        this.batchId=batchId;
        this.controlId=controlId;
    }
    public BatchWithGr(List<BatchData> batchDataList, Long stockId, String batchId) {
        this.batchId=batchId;
        this.controlId=stockId;
        this.batchDataList=batchDataList;
    }

    //pchallan
    public BatchWithGr(Long controlId,String pchallanRef) {
        this.pchallanRef=pchallanRef;
        this.controlId=controlId;
    }
    public BatchWithGr(List<BatchData> batchDataList,  String pchallanRef,Long stockId) {
        this.pchallanRef=pchallanRef;
        this.controlId=stockId;
        this.batchDataList=batchDataList;
    }
}
