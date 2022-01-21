package com.main.glory.model.paymentTerm.request;

import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.PartyDataByInvoiceNumber;
import com.main.glory.model.dispatch.request.PartyWithBatchByInvoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class GetPendingDispatch {
    String invoicNo;
    String date;
    Double amt;
    Double discount;
    Double cgst;
    Double sgst;
    Double taxAmt;
    Double netAmt;



    public GetPendingDispatch(DispatchMast dispatchMast) {

        this.discount = dispatchMast.getDiscount();
        this.cgst =dispatchMast.getCgst();
        this.sgst=dispatchMast.getSgst();
        this.netAmt = dispatchMast.getNetAmt();
        this.taxAmt = dispatchMast.getTaxAmt();

    }

    //@Query("select new com.main.glory.model.paymentTerm.request.GetPendingDispatch(dm.postfix,dm.createdDate,(dm.taxAmt+dm.discount) as AMT,dm.discount,dm.cgst,dm.sgst,dm.taxAmt,dm.netAmt) from DispatchMast dm where dm.party.id=:partyId")
    public GetPendingDispatch(String invoicNo, Date date, Double amt, Double discount, Double cgst, Double sgst, Double taxAmt, Double netAmt) {
        this.invoicNo = invoicNo;
        this.date = date.toString();
        this.amt = amt;
        this.discount = discount;
        this.cgst = cgst;
        this.sgst = sgst;
        this.taxAmt = taxAmt;
        this.netAmt = netAmt;
    }

}
