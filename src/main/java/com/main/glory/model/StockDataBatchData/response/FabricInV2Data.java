package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class FabricInV2Data {
    String batchId;
    String pchallanRef;
    String qualityName;
    String qualityId;
    Long totalPcs;
    Double totalMtr;
    Double totalWt;
    String partyName;
    String partyCode;
    Double jobCharge;
    Double billingValue;

    public FabricInV2Data(String batchId, String pchallanRef, String qualityName, String qualityId, Long totalPcs, Double totalMtr, Double totalWt, String partyName, String partyCode, Double jobCharge, Double billingValue) {
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.qualityName = qualityName;
        this.qualityId = qualityId;
        this.totalPcs = totalPcs;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.partyName = partyName;
        this.partyCode = partyCode;
        this.jobCharge = jobCharge;
        this.billingValue = StockBatchServiceImpl.changeInFormattedDecimal(billingValue);
    }
}
