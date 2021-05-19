package com.main.glory.model.quality.response;

import com.main.glory.model.dispatch.response.QualityWithRateAndTotalMtr;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityReport {
    Long id;
    String qualityId;
    String qualityName;
    Double rate;//at a time of creating invoice
    Double totalFinishMtr;
    Double totalAmt;

    public QualityReport(QualityWithRateAndTotalMtr qualityWithRateAndTotalMtr, Double totalMtr) {
        this.id=qualityWithRateAndTotalMtr.getQualityEntryId();
        this.qualityId=qualityWithRateAndTotalMtr.getQualityId();
        this.qualityName =qualityWithRateAndTotalMtr.getQualityName();
        this.rate = qualityWithRateAndTotalMtr.getRate();
        this.totalFinishMtr = totalMtr;
        this.totalAmt = totalMtr*qualityWithRateAndTotalMtr.getRate();
    }
}
