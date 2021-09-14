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
    String billingUnit;
    List<BatchData> batchDataList;


    public BatchWithGr(GetBatchByInvoice batch) {
        this.batchId=batch.getBatchId();
        this.controlId=batch.getStockId();
        this.pchallanRef = batch.getPchallanRef();
    }

    public BatchWithGr(String batchId,Long controlId,String pchallanRef) {
        this.batchId=batchId;
        this.controlId=controlId;
        this.pchallanRef = pchallanRef;
    }
    public BatchWithGr(List<BatchData> batchDataList, Long stockId, String batchId,String pchallanRef) {
        this.batchId=batchId;
        this.controlId=stockId;
        this.batchDataList=batchDataList;
        this.pchallanRef = pchallanRef;
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
