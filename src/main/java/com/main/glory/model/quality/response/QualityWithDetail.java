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

    public QualityWithDetail(Optional<QualityName> qualityName, Double availableStockValueByPartyIdWithQualityEntryId) {
        this.qualityNameId=qualityName.get().getId();
        this.qualityName=qualityName.get().getQualityName();
        this.rate =qualityName.get().getRate();
        this.stockInHand = availableStockValueByPartyIdWithQualityEntryId==null?0:availableStockValueByPartyIdWithQualityEntryId;
    }
}
