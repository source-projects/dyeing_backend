package com.main.glory.model.StockDataBatchData.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FabricInMast {
    @JsonIgnore
    Long partyId;
    @JsonIgnore
    Long qualityEntryId;
    String partyName;
    String partyCode;
    List<PendingBatchData> list;

    public FabricInMast(StockMast e) {
        this.partyId = e.getParty().getId();
        this.qualityEntryId = e.getQuality().getId();
        this.partyName = e.getParty().getPartyName();
        this.partyCode = e.getParty().getPartyCode();
    }

}
