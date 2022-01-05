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
public class FabricInV2Mast {
    @JsonIgnore
    Long id;
    String masterName;
    Double totalMtr;
    Double totalWt;
    Long totalPcs;
    List<FabricInV2Data> list;

    public FabricInV2Mast(StockMast e) {
        this.masterName = e.getParty().getUserHeadData().getFirstName();
        this.id = e.getParty().getUserHeadData().getId();
    }

    public FabricInV2Mast(StockMast e, Long totalPcs, Double totalMtr, Double totalWt) {
        this.masterName = e.getParty().getUserHeadData().getFirstName();
        this.id = e.getParty().getUserHeadData().getId();
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.totalPcs = totalPcs;
    }

    public void addTotalMtr(Double totalMtr) {
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(this.totalMtr+totalMtr);

    }

    public void addTotalWt(Double totalWt) {
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt+this.totalWt);
    }

    public void addTotalPcs(Long totalPcs) {
        this.totalPcs = this.totalPcs+totalPcs;
    }
}
