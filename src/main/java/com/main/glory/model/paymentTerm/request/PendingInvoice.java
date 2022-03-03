package com.main.glory.model.paymentTerm.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class PendingInvoice {
    String invoiceNo;

    //@Query("select dm.postfix from DispatchMast dm where dm.paymentBunchId = :paymentBunchId and dm.paymentBunchId IS NOT NULL")
    public PendingInvoice(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
