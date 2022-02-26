package com.main.glory.model.paymentTerm.request;

import com.main.glory.model.paymentTerm.PaymentData;
import com.main.glory.model.paymentTerm.PaymentMast;
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
    List<GetPendingDispatch> pendingDispatchListWithExisting;
    List<GetAdvancePayment> advancePaymentListWithExisting;


    public AddPaymentMast(PaymentMast paymentMastExist) {
        this.id = paymentMastExist.getId();
        this.partyId = paymentMastExist.getParty().getId();
        this.totalBill = paymentMastExist.getTotalBill();
        this.GstAmt = paymentMastExist.getGstAmt();
        this.rdAmt = paymentMastExist.getRdAmt();
        this.rdDetail = paymentMastExist.getRdDetail();
        this.cdAmt = paymentMastExist.getCdAmt();
        this.cdDetail = paymentMastExist.getCdDetail();
        this.amtToPay = paymentMastExist.getAmtToPay();
        this.amtPaid = paymentMastExist.getAmtPaid();
        this.otherDiff = paymentMastExist.getOtherDiff();
        this.diffDetail = paymentMastExist.getDiffDetail();
        this.createdBy = paymentMastExist.getCreatedBy()==null?null:paymentMastExist.getCreatedBy().getId();
        this.updatedBy = paymentMastExist.getUpdatedBy()==null?null:paymentMastExist.getUpdatedBy().getId();
        this.tdsAmt = paymentMastExist.getTdsAmt();
        this.tdsDetail = paymentMastExist.getTdsDetail();
        this.paymentData = paymentMastExist.getPaymentData();
    }
}
