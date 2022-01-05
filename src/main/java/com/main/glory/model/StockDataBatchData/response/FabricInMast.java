package com.main.glory.model.StockDataBatchData.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
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
public class FabricInMast {
    @JsonIgnore
    Long partyId;
    String partyName;
    String partyCode;
    String masterName;
    Double totalMtr;
    Double totalWt;
    Long totalPcs;
    Double totalBilling;
    List<FabricInData> list;

    public FabricInMast(StockMast e) {
        this.partyId = e.getParty().getId();
        this.partyName = e.getParty().getPartyName();
        this.partyCode = e.getParty().getPartyCode();
        this.masterName = e.getParty().getUserHeadData().getFirstName();
    }

    public FabricInMast(StockMast e, Double totalMtr, Double totalWt, Long totalPcs,Double totalBilling) {
        this.partyId = e.getParty().getId();
        this.partyName = e.getParty().getPartyName();
        this.partyCode = e.getParty().getPartyCode();
        this.masterName = e.getParty().getUserHeadData().getFirstName();
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWt=StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.totalPcs = totalPcs;
        this.totalBilling = StockBatchServiceImpl.changeInFormattedDecimal(totalBilling);
    }

    public void addTotalMtr(Double totalMtr) {
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr+this.totalMtr);
    }

    public void addTotalWt(Double totalWt) {
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt+this.totalWt);
    }

    public void addTotalPcs(Long totalPcs) {
        this.totalPcs = this.totalPcs + totalPcs;
    }

    public void addBillingValues(Double billingValues) {
        this.totalBilling = StockBatchServiceImpl.changeInFormattedDecimal(billingValues+this.totalBilling);
    }
}
