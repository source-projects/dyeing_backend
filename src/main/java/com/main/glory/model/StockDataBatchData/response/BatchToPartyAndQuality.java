package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchToPartyAndQuality {
    Long controlId;
    String batchId;
    Long partyId;
    String partyName;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    Double totalWt;
    Double totalMtr;
    String processName;
    String partyShadeNo;


    public BatchToPartyAndQuality(Quality quality, Party party, GetBatchWithControlId batch) {
        this.qualityEntryId=quality.getId();
        this.qualityName=quality.getQualityName();
        this.qualityId=quality.getQualityId();
        this.partyId=party.getId();
        this.partyName=party.getPartyName();
        this.controlId=batch.controlId;
        this.batchId=batch.batchId;
        this.totalMtr=batch.MTR;
        this.totalWt=batch.WT;

    }


}
