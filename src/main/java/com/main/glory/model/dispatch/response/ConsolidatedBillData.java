package com.main.glory.model.dispatch.response;

import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
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
    String qualityId;
    Long invoiceNo;
    //new field
    Double discount;
    Double percentageDiscount;
    Double taxAmt;
    Double cgst;
    Double sgst;
    Double igst;
    Double gstAmt;
    Double netAmt;
    String partyAddress1;
    String partyAddress2;
    String contactNo;
    Double discountAmt;
    private String city;
    private String state;
    private String gstin;
    String billingUnit;
    String inwardUnit;


    public ConsolidatedBillData(Party party, GetQualityResponse quality, String batchId, Long pcs, Double totalBatchMtr, Double totalFinishMtr, Double amt, Double rate, DispatchMast dispatchMast,Long greyPcs) {

        this.batchId = batchId;
        this.invoiceDate = dispatchMast.getCreatedDate();
        this.invoiceNo = dispatchMast.getPostfix();
        this.partyName = party.getPartyName();
        this.qualityName = quality.getQualityName();
        this.qualityId = quality.getQualityId();
        this.pcs =pcs;
        this.greyPcs = greyPcs;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalBatchMtr);
        this.totalFinishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.rate = rate;
        this.amt = StockBatchServiceImpl.changeInFormattedDecimal(amt);
        this.partyId = party.getId();
        this.qualityEntryId=quality.getId();
        this.qualityNameId = quality.getQualityNameId();
        this.discount=dispatchMast.getDiscount();
        this.percentageDiscount=dispatchMast.getPercentageDiscount()==null?0:dispatchMast.getPercentageDiscount();
        this. netAmt=dispatchMast.getNetAmt();
        this.partyAddress1 = party.getPartyAddress1()==null?null:party.getPartyAddress1();
        this.partyAddress2 = party.getPartyAddress2()==null?null:party.getPartyAddress2();
        this.gstin = party.getGSTIN()==null?null:party.getGSTIN();
        this.city = party.getCity()==null?null:party.getCity();
        this.state = party.getState()==null?null:party.getState();
        this.contactNo = party.getContactNo()==null?null:party.getContactNo();
        this.discountAmt = StockBatchServiceImpl.changeInFormattedDecimal((this.amt * this.percentageDiscount)/100);
        this.taxAmt = StockBatchServiceImpl.changeInFormattedDecimal(this.amt - ((this.amt * this.percentageDiscount)/100));
        this.igst=this.state.equalsIgnoreCase("Gujarat")?0:StockBatchServiceImpl.changeInFormattedDecimal((this.taxAmt * 5)/100);
        this.cgst=this.state.equalsIgnoreCase("Gujarat")?StockBatchServiceImpl.changeInFormattedDecimal((this.taxAmt * 2.5)/100):0;
        this.sgst=this.state.equalsIgnoreCase("Gujarat")?StockBatchServiceImpl.changeInFormattedDecimal((this.taxAmt * 2.5)/100):0;
        this.gstAmt=this.cgst+this.sgst+this.igst;
        this.netAmt = this.taxAmt+this.gstAmt;
    }
}
