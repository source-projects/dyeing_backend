package com.main.glory.model.dispatch.response.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedBillDataForExcel {
    String invoiceNo;
    String invoiceDate;
    String partyName;
    String partyAddress1;
    String partyAddress2;

    private String city;
    private String state;
    private String gstin;
    String contactNo;
    String batchId;
    String qualityId;
    String qualityName;
    Long pcs;
    Long greyPcs;
    Double totalMtr;
    Double totalFinishMtr;

    Double rate;
    Double amt;
    @JsonIgnore
    Long partyId;
    @JsonIgnore
    Long qualityEntryId;
    @JsonIgnore
    Long qualityNameId;


    //new field
    Double discount;
    Double percentageDiscount;
    Double taxAmt;
    Double cgst;
    Double sgst;
    Double igst;
    Double gstAmt;
    Long netAmt;
    Double discountAmt;
    String billingUnit;
    String inwardUnit;
    String deliveryMode;
    Double sharinkage;
    Double wtPer100m;
    String headName;

    @JsonIgnore
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    //this one is using through query
    public ConsolidatedBillDataForExcel(String batchId, Long pcs, Date invoiceDate, String invoiceNo, String qualityId,
                                        Double discount, Double percentageDiscount, Double netAmt,
                                        Double totalFinishMtr, Double totalMtr, Double rate,
                                        String city, String state, String gstin, String partyName, String  partyAddress1, String partyAddress2,
                                        String contactNo, String billingUnit, String inwardUnit, String deliveryMode, Double wtPer100m, String qualityName,
                                        String headName,
                                        Double amt, Double discountAmt, Double taxAmt)
    {
        //this.greyPcs = greyPcs;
        this.pcs = pcs;
        this.batchId = batchId;
        this.invoiceDate = dateFormat.format(invoiceDate);
        this.invoiceNo =invoiceNo;
        this.qualityId = qualityId;
        this.billingUnit = billingUnit;
        this.inwardUnit = inwardUnit;
        this.city= city;
        this.state = state;
        this.gstin = gstin;
        this.partyName= partyName;
        this.partyAddress1 = partyAddress1;
        this.partyAddress2=partyAddress2;
        this.contactNo = contactNo;
        this.deliveryMode = deliveryMode;
        this.wtPer100m = wtPer100m;
        this.qualityName = qualityName;
        this.headName = headName;
        this.discount=discount;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalFinishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.rate = rate;
        this.amt = totalFinishMtr*rate;
        //this.greyPcs = greyPcs;
        this. percentageDiscount=percentageDiscount;
        this.discountAmt = (this.amt/100)*this.percentageDiscount;

        //this.netAmt=netAmt;
        this.taxAmt = this.amt - this.discountAmt;
        this.igst=this.state.equalsIgnoreCase("Gujarat")?0:StockBatchServiceImpl.changeInFormattedDecimal((this.taxAmt * 5)/100);
        this.cgst=this.state.equalsIgnoreCase("Gujarat")?StockBatchServiceImpl.changeInFormattedDecimal((this.taxAmt * 2.5)/100):0;
        this.sgst=this.state.equalsIgnoreCase("Gujarat")?StockBatchServiceImpl.changeInFormattedDecimal((this.taxAmt * 2.5)/100):0;
        this.gstAmt=this.cgst+this.sgst+this.igst;
        this.netAmt = Math.round(this.taxAmt+this.gstAmt);
        this.sharinkage = StockBatchServiceImpl.changeInFormattedDecimal(((this.totalMtr - this.totalFinishMtr)/this.totalMtr) * 100);


    }


    public ConsolidatedBillDataForExcel(Party party, Quality quality, String batchId, Long pcs, Double totalBatchMtr, Double totalFinishMtr, Double amt, Double rate, DispatchMast dispatchMast, Long greyPcs) {
        this.deliveryMode = dispatchMast.getDeliveryMode()==null?null:dispatchMast.getDeliveryMode();
        this.batchId = batchId;
        this.invoiceDate = dateFormat.format(dispatchMast.getCreatedDate());
        this.invoiceNo =dispatchMast.getPostfix();
        this.partyName = party.getPartyName();
        this.qualityName = quality.getQualityName().getQualityName();
        this.qualityId = quality.getQualityId();
        this.pcs =pcs;
        this.greyPcs = greyPcs;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalBatchMtr);
        this.totalFinishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.rate = rate;
        this.amt = StockBatchServiceImpl.changeInFormattedDecimal(amt);
        this.partyId = party.getId();
        this.qualityEntryId=quality.getId();
        this.qualityNameId = quality.getQualityName().getId();
        this.discount=dispatchMast.getDiscount();
        this.percentageDiscount=dispatchMast.getPercentageDiscount()==null?0:dispatchMast.getPercentageDiscount();
        this. netAmt=dispatchMast.getNetAmt().longValue();
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
        this.netAmt = Math.round(this.taxAmt+this.gstAmt);
        this.sharinkage = StockBatchServiceImpl.changeInFormattedDecimal(((this.totalMtr - this.totalFinishMtr)/this.totalMtr) * 100);

    }
}
