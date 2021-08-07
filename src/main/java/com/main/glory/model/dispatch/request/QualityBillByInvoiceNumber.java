package com.main.glory.model.dispatch.request;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;


@NoArgsConstructor
@Getter
@Setter
public class QualityBillByInvoiceNumber {
    String qualityId;
    String qualityName;
    String hsn;
    Double rate;
    Double shadeRate;
    String batchId;
    String pchallanRef;
    Double totalMtr;
    Double finishMtr;
    Long pcs;
    String pChalNo;
    Double amt;
    Double amtWt;
    String billingUnit;


    public QualityBillByInvoiceNumber(String qualityId, String qualityName, String hsn, Double rate, String batchId, Double totalMtr, Double finishMtr, Long pcs, String pChalNo) {
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.hsn = hsn;
        this.rate = rate;
        this.batchId = batchId;
        this.totalMtr = totalMtr;
        this.finishMtr = finishMtr;
        this.pcs = pcs;
        this.pChalNo = pChalNo;
        this.amt = finishMtr*rate;
    }

    public QualityBillByInvoiceNumber(Quality quality) {
        this.qualityId=quality.getQualityId();
        this.rate=quality.getRate();
        this.qualityName=quality.getQualityName();
        this.hsn=quality.getHsn();
    }


    public QualityBillByInvoiceNumber(Quality quality, Double totalFinishMtr, Double totalMtr, Long totalPcs, Optional<QualityName> qualityName, String batchId, StockMast stockMast) {
        this.qualityId = quality.getQualityId();
        this.qualityName = qualityName.get().getQualityName();
        this.hsn = quality.getHsn();
        this.rate = quality.getRate();
        this.batchId = batchId;
        this.totalMtr = quality.getBillingUnit().equalsIgnoreCase("meter")==true?totalMtr:(totalMtr/100)*stockMast.getWtPer100m();
        this.finishMtr = quality.getBillingUnit().equalsIgnoreCase("meter")==true?totalFinishMtr:(totalFinishMtr/100)*stockMast.getWtPer100m();;
        this.pcs = totalPcs;
        this.pChalNo = stockMast.getChlNo();
        this.amt = this.finishMtr*rate;
        this.pchallanRef = batchId;// batch id can be batch id or pchallan ref
    }
}
