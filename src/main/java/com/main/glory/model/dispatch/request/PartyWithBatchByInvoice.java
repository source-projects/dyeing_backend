package com.main.glory.model.dispatch.request;

import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
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
public class PartyWithBatchByInvoice {

    Long partyId;
    String partyName;
    List<BatchWithTotalMTRandFinishMTR> batchWithControlIdList;

    public PartyWithBatchByInvoice(List<BatchWithTotalMTRandFinishMTR> batchWithTotalMTRandFinishMTRList, Party party) {
        this.partyId=party.getId();
        this.partyName=party.getPartyName();
        this.batchWithControlIdList=batchWithTotalMTRandFinishMTRList;
    }
}
