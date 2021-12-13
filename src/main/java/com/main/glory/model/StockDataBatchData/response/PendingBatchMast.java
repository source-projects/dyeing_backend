package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingBatchMast {
    @JsonIgnore
    Long partyId;
    @JsonIgnore
    Long qualityEntryId;
    String partyName;
    String partyCode;
    String headName;
    @JsonIgnore
    String qualityId;
    @JsonIgnore
    String qualityName;
    Double totalQualityMeter;
    List<PendingBatchData> list;

    public PendingBatchMast(Party party) {
        this.partyId = party.getId();
        this.partyName = party.getPartyName();
        this.partyCode = party.getPartyCode();
        this.headName = party.getUserHeadData().getUserName();
    }


    public PendingBatchMast(StockMast e) {
        this.partyId = e.getParty().getId();
        this.partyName = e.getParty().getPartyName();
        this.partyCode = e.getParty().getPartyCode();
        this.headName = e.getParty().getUserHeadData().getUserName();
        this.qualityId=e.getQuality().getQualityId();
        this.qualityName=e.getQuality().getQualityName().getQualityName();
        this.qualityEntryId = e.getQuality().getId();
    }

    public void addTotalQualityList(Double totalMtr) {
        this.totalQualityMeter = this.totalQualityMeter+totalMtr;
    }
}
