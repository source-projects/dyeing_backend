package com.main.glory.model.dispatch.request;

import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PartyDataByInvoiceNumber {
    String partyName;
    String address;
    String GST;
    Long invoiceNo;
    Date invoiceDate;
    Double discount;
    Double cgst;
    Double sgst;
    Double igst;
    Double taxAmt;
    Double netAmt;
    Double percentageDiscount;
    String remark;
    List<QualityBillByInvoiceNumber> qualityList;
    List<BatchWithGr> batchWithGrList;
    Date createdDate;
    String state;
    private String contactNo;


    public PartyDataByInvoiceNumber(Party party, List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList, List<BatchWithGr> batchWithGrList, DispatchMast dispatchMast) {
        this.partyName=party.getPartyName();
        this.contactNo=party.getContactNo();
        this.address=party.getPartyAddress1();
        this.GST=party.getGSTIN();
        this.qualityList=qualityBillByInvoiceNumberList;
        this.batchWithGrList=batchWithGrList;
        this.discount = dispatchMast.getDiscount();
        this.cgst = dispatchMast.getCgst();
        this.sgst = dispatchMast.getSgst();
        this.igst = dispatchMast.getIgst();
        this.taxAmt=dispatchMast.getTaxAmt();
        this.netAmt = Math.floor(dispatchMast.getNetAmt());
        this.percentageDiscount = dispatchMast.getPercentageDiscount();
        this.remark = dispatchMast.getRemark();
    }
    public PartyDataByInvoiceNumber(Party party, List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList, List<BatchWithGr> batchWithGrList) {
        this.partyName=party.getPartyName();
        this.state = party.getState();
        this.contactNo=party.getContactNo();
        this.address=party.getPartyAddress1();
        this.GST=party.getGSTIN();
        this.qualityList=qualityBillByInvoiceNumberList;
        this.batchWithGrList=batchWithGrList;
        this.percentageDiscount = party.getPercentageDiscount();
    }

}

