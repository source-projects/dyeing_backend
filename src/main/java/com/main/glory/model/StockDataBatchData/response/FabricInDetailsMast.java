package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
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
public class FabricInDetailsMast {

    @JsonIgnore
    Long id;
    String masterName;
    Double totalMtr;
    Double totalWt;
    Double totalBillingValue;
    Long totalPcs;
    @JsonIgnore
    Long countObject;
    List<FabricInDetailsData> list;

    public FabricInDetailsMast(StockMast e) {
        this.id = e.getParty().getUserHeadData().getId();
        this.masterName = e.getParty().getUserHeadData().getFirstName();
    }

    public FabricInDetailsMast(StockMast e, Double totalMtr, Double totalWt, Double totalBillingValues,Long totalPcs) {
        this.id = e.getParty().getUserHeadData().getId();
        this.masterName = e.getParty().getUserHeadData().getFirstName();
        this.totalBillingValue = StockBatchServiceImpl.changeInFormattedDecimal(totalBillingValues);
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalPcs = totalPcs;
    }

    public FabricInDetailsMast(FabricInData e) {
        this.id = e.getUserData().getId();
        this.masterName = e.getUserData().getFirstName();
        this.totalBillingValue = e.getBillingValue();
        this.totalWt = e.getTotalWt();
        this.totalPcs = e.getTotalPcs();
        this.totalMtr = e.getTotalMtr();
    }

    public FabricInDetailsMast(UserData key, Double totalMtr, Double totalWt,Long totalPcs,Double totalBillingValue) {
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.masterName = key.getFirstName();
        this.id = key.getId();
        this.totalPcs = totalPcs;
        this.totalBillingValue = StockBatchServiceImpl.changeInFormattedDecimal(totalBillingValue);
    }

    public void addTotalMtr(Double totalMtr) {
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr+this.totalMtr);
    }

    public void addTotalWt(Double totalWt) {
        this.totalWt =StockBatchServiceImpl.changeInFormattedDecimal(totalWt+this.totalWt);
    }

    public void addTotalBillingValue(Double totalBillingValues) {
        this.totalBillingValue=StockBatchServiceImpl.changeInFormattedDecimal(totalBillingValues+totalBillingValues);
    }


    public void addTotalPcs(Long totalPcs) {
        this.totalPcs +=totalPcs;
    }
}
