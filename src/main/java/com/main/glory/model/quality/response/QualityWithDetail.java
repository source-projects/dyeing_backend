package com.main.glory.model.quality.response;

import com.main.glory.model.quality.QualityName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityWithDetail {
    Long qualityNameId;
    String qualityName;
    Double rate;
    Double stockInHand;

    public QualityWithDetail(QualityName qualityName, Double availableStockValueByPartyIdWithQualityEntryId) {
        this.qualityNameId=qualityName.getId();
        this.qualityName=qualityName.getQualityName();
        this.rate =qualityName.getRate();
        this.stockInHand = availableStockValueByPartyIdWithQualityEntryId==null?0:availableStockValueByPartyIdWithQualityEntryId;
    }
}
