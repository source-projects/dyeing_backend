package com.main.glory.model.dispatch.request;

import com.main.glory.model.quality.Quality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityBillByInvoiceNumber {
    String qualityId;
    String qualityName;
    String hsn;
    Double rate;
    String batchId;
    Double totalMtr;
    Double finishMtr;
    Long pcs;
    Long pChalNo;
    Double amt;



    public QualityBillByInvoiceNumber(Quality quality) {
        this.qualityId=quality.getQualityId();
        this.rate=quality.getRate();
        this.qualityName=quality.getQualityName();
        this.hsn=quality.getHSN();
    }
}
