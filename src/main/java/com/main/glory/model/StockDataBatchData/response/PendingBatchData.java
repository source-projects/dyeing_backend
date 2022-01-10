package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingBatchData {
    String qualityId;
    String qualityName;
    String pchallanRef;
    String batchId;
    Long totalPcs;
    String receiveDate;
    Double totalBatchMtr;
    Double totalBatchWt;

    @JsonIgnore
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public PendingBatchData(String batchId, String pchallanRef, Double totalBatchMtr, Double totalBatchWt,Date receiveDate,String qualityName,Long totalPcs,String qualityId) {
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.totalBatchMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalBatchMtr);
        this.totalBatchWt = StockBatchServiceImpl.changeInFormattedDecimal(totalBatchWt);
        this.receiveDate = formatter.format(receiveDate);
        this.qualityId = qualityId;
        this.totalPcs=totalPcs;
        this.qualityName = qualityName;
        //this.totalQualityMeter = this.getTotalQualityMeter()==null?this.totalBatchMtr:this.totalQualityMeter+this.totalBatchMtr;

    }
}
