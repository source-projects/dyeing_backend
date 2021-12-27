package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
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
    Double totalQualityWt;
    Long totalPcs;
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

    public void addTotalQualityMeter(Double totalMtr) {
        this.totalQualityMeter = StockBatchServiceImpl.changeInFormattedDecimal(this.totalQualityMeter+totalMtr);
    }

    public void addTotalQualityWt(Double totalQualityWt) {
        this.totalQualityWt = StockBatchServiceImpl.changeInFormattedDecimal(this.totalQualityWt+totalQualityWt);
    }

    public void addTotalQualityPcs(Long totalPcs) {
        this.totalPcs = this.totalPcs+totalPcs;
    }
}
