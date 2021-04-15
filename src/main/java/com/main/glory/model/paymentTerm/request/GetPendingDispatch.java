package com.main.glory.model.paymentTerm.request;

import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.PartyDataByInvoiceNumber;
import com.main.glory.model.dispatch.request.PartyWithBatchByInvoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
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

}
