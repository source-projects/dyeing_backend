package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.user.UserData;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class FabricInData {

    String qualityName;
    String qualityId;
    String batchId;
    String pchallanRef;
    Long totalPcs;
    Double totalMtr;
    Double totalWt;
    Double rate;
    Double billingValue;
    @JsonIgnore
    Party party;
    @JsonIgnore
    UserData userData;



    public FabricInData(String batchId, String pchallanRef,String qualityName, String qualityId, Long totalPcs, Double totalMtr, Double totalWt, Double rate, Double billingValue) {
        this.qualityName = qualityName;
        this.qualityId = qualityId;
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.totalPcs = totalPcs;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWt = StockBatchServiceImpl.changeInFormattedDecimal(totalWt);
        this.rate = rate;
        this.billingValue = StockBatchServiceImpl.changeInFormattedDecimal(billingValue);
    }

    //select new com.main.glory.model.StockDataBatchData.response.FabricInData(b.batchId,b.pchallanRef,s.quality.qualityName.qualityName,s.quality.qualityId,count(b.id),SUM(b.mtr),SUM(b.wt),s.quality.rate,(s.quality.rate * SUM(b.mtr) * 0.9) as billingValues,s.party,s.party.userHeadData) from BatchData b INNER JOIN StockMast s on b.controlId = s.id  where (:from IS NULL OR DATE(s.createdDate)>=DATE(:from)) AND (:to IS NULL OR DATE(s.createdDate)<=DATE(:to)) AND (:partyId IS NULL OR s.party.id=:partyId) AND (:qualityNameId IS NULL OR s.quality.qualityName.id=:qualityNameId) AND (:qualityEntryId IS NULL OR s.quality.id=:qualityEntryId) AND (:userHeadId IS NULL OR s.party.userHeadData.id=:userHeadId) AND b.isExtra=false GROUP BY b.batchId,b.pchallanRef
    public FabricInData(String batchId, String pchallanRef,String qualityName, String qualityId, Long totalPcs, Double totalMtr, Double totalWt, Double rate, Double billingValue,Party party,UserData userData) {
        this.qualityName = qualityName;
        this.qualityId = qualityId;
        this.batchId = batchId;
        this.pchallanRef = pchallanRef;
        this.totalPcs = totalPcs;
        this.totalMtr = (totalMtr);
        this.totalWt = (totalWt);
        this.rate = rate;
        this.billingValue = (billingValue);
        this.userData = userData;
        this.party = party;
    }


}
