package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.BatchData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
public class GetAllBatchResponse {
    Double totalMtr;
    Double totalWt;
    String batchId;
    public GetAllBatchResponse(Double totalMtr, Double totalWt, String batchId) {
        this.totalMtr = totalMtr==null?0:totalMtr;
        this.totalWt = totalWt==null?0:totalWt;
        this.batchId = batchId;
    }

    String qualityName;

    public void updateGetAllBatchResponse(BatchData batchData){
        if(batchData.getMtr()!=null)this.totalMtr+=batchData.getMtr();
        if(batchData.getWt()!=null)this.totalWt+=batchData.getWt();
    }


    public GetAllBatchResponse(Double totalMtr, Double totalWt, String batchId, String qualityName) {
        this.totalMtr = totalMtr;
        this.totalWt = totalWt;
        this.batchId = batchId;
        this.qualityName = qualityName;
    }
}
