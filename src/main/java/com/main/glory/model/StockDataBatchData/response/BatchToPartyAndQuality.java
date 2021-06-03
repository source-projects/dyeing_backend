package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchToPartyAndQuality {
    String mergeBatchId;
    Long controlId;
    String batchId;
    String partyId;
    String partyName;
    String qualityEntryId;
    String qualityId;
    String qualityName;
    Double totalWt;
    Double totalMtr;
    String processName;
    String partyShadeNo;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public BatchToPartyAndQuality(Quality quality, Party party, GetBatchWithControlId batch, QualityName qualityName) {
        this.qualityEntryId=quality.getId().toString();
        this.qualityName=qualityName.getQualityName();
        this.qualityId=quality.getQualityId();
        this.partyId=party.getId().toString();
        this.partyName=party.getPartyName();
        this.controlId=batch.controlId;
        this.batchId=batch.batchId;
        this.totalMtr=Double.valueOf(df2.format(batch.MTR));
        this.totalWt=Double.valueOf(df2.format(batch.WT));
        this.mergeBatchId = batch.getMergeBatchId()==null?null : batch.getMergeBatchId();

    }


}
