package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityWithRateAndTotalMtr {
    Long qualityEntryId;
    String qualityName;
    String qualityId;
    Double totalMtr;
    Double rate;
    Double totalAmt;

    public QualityWithRateAndTotalMtr(Long qualityEntryId, String qualityName, String qualityId, Double rate) {
        this.qualityEntryId = qualityEntryId;
        this.qualityName = qualityName;
        this.qualityId = qualityId;
        this.rate = rate;
    }

}
