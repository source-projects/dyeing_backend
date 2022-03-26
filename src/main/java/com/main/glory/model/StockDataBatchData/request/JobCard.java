package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.user.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobCard {
    Long partyId;
    Double percentageDiscount;
    String partyCode;
    String partyName;
    String masterName;
    Long userHeadId;
    Date receiveDate;
    Date chlDate;
    String batchId;
    String chalNo;
    String qualityName;
    String qualityId;
    Long qualiyEntryId;
    Double totalMtr;
    Double totalWt;
    Double totalFinishMtr;
    Long totalPcs;
    Double wtPer100m;
    String unit;
    List<BatchData> batchDataList;


    public JobCard(StockMast stockMast, Party party, UserData userData, Quality quality, QualityName qualityName, Double totalMtr, Long totalPcs, Double totalWt) {
        this.partyId=party.getId();
        this.partyName = party.getPartyName();
        this.masterName=userData.getUserName();
        this.userHeadId = userData.getId();
        this.receiveDate = stockMast.getReceiveDate();
        this.chlDate = stockMast.getChlDate();
        this.chalNo=stockMast.getChlNo();
        this.qualityName=qualityName.getQualityName();
        this.qualityId=quality.getQualityId();
        this.qualiyEntryId = quality.getId();
        this.totalMtr=totalMtr;
        this.totalWt=totalWt;
        this.totalPcs=totalPcs;
        this.partyCode=party.getPartyCode();
        this.percentageDiscount = party.getPercentageDiscount()==null?0:party.getPercentageDiscount();
        this.wtPer100m = stockMast.getWtPer100m();
        this.unit = stockMast.getUnit();
    }
}
