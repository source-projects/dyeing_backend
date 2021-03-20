package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

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
    String colorName;




    public GetBatchDetailByProduction(Party party, Double totalWt, Optional<ShadeMast> shadeMast, StockMast stockMast, String batchId) {
        this.partyName=party.getPartyName();
        this.partyShadeNo=(shadeMast.isPresent())?shadeMast.get().getPartyShadeNo():null;
        this.colorName=shadeMast.isPresent()?shadeMast.get().getColorName():null;
        this.batchId=batchId;
        this.stockId=stockMast.getId();
        this.colorTone=shadeMast.isPresent()?shadeMast.get().getColorTone():null;
        this.totalWt=totalWt;
    }

    public GetBatchDetailByProduction(Party party, Double totalWt, StockMast stockMast, String batchId) {
        this.partyName=party.getPartyName();
        this.batchId=batchId;
        this.stockId=stockMast.getId();
        this.totalWt=totalWt;
    }
}
