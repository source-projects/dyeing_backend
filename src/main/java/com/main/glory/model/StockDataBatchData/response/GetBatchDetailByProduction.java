package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBatchDetailByProduction {

    String partyName;
    String partyShadeNo;
    String batchId;
    Long stockId;
    String colorTone;
    Double totalWt;




    public GetBatchDetailByProduction(Party party, Double totalWt, ShadeMast shadeMast, StockMast stockMast, String batchId) {
        this.partyName=party.getPartyName();
        this.partyShadeNo=shadeMast.getPartyShadeNo();
        this.batchId=batchId;
        this.stockId=stockMast.getId();
        this.colorTone=shadeMast.getColorTone();
        this.totalWt=totalWt;
    }
}
