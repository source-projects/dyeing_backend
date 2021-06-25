package com.main.glory.model.dispatch.response;

import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedBillData {
    String batchId;
    Date invoiceDate;
    String partyName;
    String qualityName;
    Long pcs;
    Long greyPcs;
    Double totalMtr;
    Double totalFinishMtr;
    Double rate;
    Double amt;
    String headName;
    Long partyId;
    Long qualityEntryId;
    Long qualityNameId;
    Long invoiceNo;
    //new field
    Double discount;
    Double percentageDiscount;
    Double taxAmt;
    Double cgst;
    Double sgst;
    Double gstAmt;
    Double netAmt;


    public ConsolidatedBillData(Party party, GetQualityResponse quality, String batchId, Long pcs, Double totalBatchMtr, Double totalFinishMtr, Double amt, Double rate, DispatchMast dispatchMast,Long greyPcs) {

        this.batchId = batchId;
        this.invoiceDate = dispatchMast.getCreatedDate();
        this.invoiceNo = dispatchMast.getPostfix();
        this.partyName = party.getPartyName();
        this.qualityName = quality.getQualityName();
        this.pcs =pcs;
        this.greyPcs = greyPcs;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalBatchMtr);
        this.totalFinishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.rate = rate;
        this.amt = StockBatchServiceImpl.changeInFormattedDecimal(amt);
        this.partyId = party.getId();
        this.qualityEntryId=quality.getId();
        this.qualityNameId = quality.getQualityNameId();
        this. discount=dispatchMast.getDiscount();
        this.percentageDiscount=dispatchMast.getPercentageDiscount();
        this.taxAmt=dispatchMast.getTaxAmt();
        this.cgst=dispatchMast.getCgst();
        this.sgst=dispatchMast.getSgst();
        this.gstAmt=this.cgst+this.sgst;
        this. netAmt=dispatchMast.getNetAmt();
    }
}
