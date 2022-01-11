package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.user.UserData;
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
public class FabricInDetailsData {
    //@JsonIgnore
    Long partyId;
    String partyName;
    String partyCode;
    Double totalMtr;
    Double totalWt;
    Double totalBillingValue;
    Long totalPcs;
    Long countObject;
    @JsonIgnore
    UserData userData;
    List<FabricInData> list;

    public FabricInDetailsData(StockMast e, Long totalPcs, Double totalMtr, Double totalWt, Double totalBillingValues) {
        this.partyId = e.getParty().getId();
        this.partyName = e.getParty().getPartyName();
        this.partyCode = e.getParty().getPartyCode();
        this.totalBillingValue = StockBatchServiceImpl.changeInFormattedDecimal(totalBillingValues);
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.totalPcs = totalPcs;
    }

    public FabricInDetailsData(FabricInData e) {
        this.partyId = e.getParty().getId();
        this.partyCode = e.getParty().getPartyCode();
        this.partyName = e.getParty().getPartyName();
        this.totalBillingValue = e.getBillingValue();
        this.totalPcs = e.getTotalPcs();
        this.totalWt = e.getTotalWt();
        this.totalMtr = e.getTotalMtr();
    }

    public FabricInDetailsData(Party key) {
        this.partyName = key.getPartyName();
        this.partyCode = key.getPartyCode();
        this.partyId = key.getId();
        this.userData=key.getUserHeadData();
    }

    public FabricInDetailsData(Party key, Long totalPcs, Double totalMtr, Double totalWt, Double billingValue) {
        this.partyName = key.getPartyName();
        this.partyCode = key.getPartyCode();
        this.partyId = key.getId();
        this.userData=key.getUserHeadData();
        this.totalWt = totalWt;
        this.totalMtr = totalMtr;
        this.totalPcs = totalPcs;
        this.totalBillingValue = billingValue;
    }

    public void addTotalMtr(Double totalMtr) {
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr+this.totalMtr);
    }

    public void addTotalWt(Double totalWt) {
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt+this.totalWt);
    }

    public void addTotalPcs(Long totalPcs) {
        this.totalPcs +=totalPcs;
    }

    public void addTotalBillingValue(Double totalBillingValues) {
        this.totalBillingValue = StockBatchServiceImpl.changeInFormattedDecimal(totalBillingValues+this.totalBillingValue);
    }
}
