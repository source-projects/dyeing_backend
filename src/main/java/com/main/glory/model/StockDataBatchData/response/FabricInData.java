package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FabricInData {

    String qualityName;
    String qualityId;
    String batchId;
    String pchallanRef;
    Long totalPcs;
    Double totalMtr;
    Double totalWt;
    Double rate;
    Double billingValue;

    public FabricInData(String batchId, String pchallanRef,String qualityName, String qualityId, Long totalPcs, Double totalMtr, Double totalWt, Double rate, Double billingValue) {
        this.qualityName = qualityName;
        this.qualityId = qualityId;
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.totalPcs = totalPcs;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.rate = rate;
        this.billingValue = StockBatchServiceImpl.changeInFormattedDecimal(billingValue);
    }
}
