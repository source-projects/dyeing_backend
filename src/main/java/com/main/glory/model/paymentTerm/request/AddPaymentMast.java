package com.main.glory.model.paymentTerm.request;

import com.main.glory.model.paymentTerm.PaymentData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddPaymentMast {

    Long id;
    Long partyId;
    Double totalBill;
    Double GstAmt;
    Double rdAmt;
    String rdDetail;
    Double cdAmt;
    String cdDetail;
    Double amtToPay;
    Double amtPaid;
    Double otherDiff;
    String diffDetail;
    Long createdBy;
    Long updatedBy;
    Double tdsAmt;
    String tdsDetail;
    List<PaymentData> paymentData;
    List<PendingInvoice> invoices;
    List<AdvancePaymentIdList> advancePayList;



}
