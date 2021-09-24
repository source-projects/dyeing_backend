package com.main.glory.model.StockDataBatchData.response;

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
    Long partyId;
    String partyName;
    String partyCode;
    List<PendingBatchData> pendingBatchDataList;

    public PendingBatchMast(Party party) {
        this.partyId = party.getId();
        this.partyName = party.getPartyName();
        this.partyCode = party.getPartyCode();
    }
}
