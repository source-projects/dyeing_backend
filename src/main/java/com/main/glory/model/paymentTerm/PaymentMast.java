package com.main.glory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.PaymentData;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class PaymentMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
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
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    Date updatedDate;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<PaymentData> paymentData;

    public PaymentMast(AddPaymentMast paymentMast) {

        this.partyId=paymentMast.getPartyId();
        this.totalBill=paymentMast.getTotalBill();
        this.GstAmt=paymentMast.getGstAmt();
        this.rdAmt=paymentMast.getRdAmt();
        this.rdDetail=paymentMast.getRdDetail();
        this.cdAmt=paymentMast.getCdAmt();
        this.cdDetail=paymentMast.getCdDetail();
        this.amtToPay=paymentMast.getAmtToPay();
        this.amtPaid=paymentMast.getAmtPaid();
        this.otherDiff=paymentMast.getOtherDiff();
        this.diffDetail=paymentMast.getDiffDetail();
        this.createdBy=paymentMast.getCreatedBy();
        this.updatedBy=paymentMast.getUpdatedBy();

    }

    public PaymentMast(PaymentMast paymentMast) {
        this.partyId=paymentMast.getPartyId();
        this.totalBill=paymentMast.getTotalBill();
        this.GstAmt=paymentMast.getGstAmt();
        this.rdAmt=paymentMast.getRdAmt();
        this.rdDetail=paymentMast.getRdDetail();
        this.cdAmt=paymentMast.getCdAmt();
        this.cdDetail=paymentMast.getCdDetail();
        this.amtToPay=paymentMast.getAmtToPay();
        this.amtPaid=paymentMast.getAmtPaid();
        this.otherDiff=paymentMast.getOtherDiff();
        this.diffDetail=paymentMast.getDiffDetail();
        this.createdBy=paymentMast.getCreatedBy();
        this.updatedBy=paymentMast.getUpdatedBy();
    }

    @PrePersist
    protected  void onCreate()
    {
        this.createdDate=new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected  void onUpdate()
    {
        this.updatedDate=new Date(System.currentTimeMillis());
    }

}
