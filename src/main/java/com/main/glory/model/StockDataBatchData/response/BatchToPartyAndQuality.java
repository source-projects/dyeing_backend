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


    public BatchToPartyAndQuality(Quality quality, Party party, GetBatchWithControlId batch) {
        this.qualityEntryId=quality.getId().toString();
        this.qualityName=quality.getQualityName();
        this.qualityId=quality.getQualityId();
        this.partyId=party.getId().toString();
        this.partyName=party.getPartyName();
        this.controlId=batch.controlId;
        this.batchId=batch.batchId;
        this.totalMtr=batch.MTR;
        this.totalWt=batch.WT;
        this.mergeBatchId = batch.getMergeBatchId()==null?null : batch.getMergeBatchId();

    }


}
