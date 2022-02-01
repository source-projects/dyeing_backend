package com.main.glory.model.paymentTerm;

import com.main.glory.model.party.Party;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import com.main.glory.model.user.UserData;

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
    
    Double totalBill;
    Double GstAmt;
    Double rdAmt;
    String rdDetail;
    Double cdAmt;
    String cdDetail;
    Double tdsAmt;
    String tdsDetail;
    Double amtToPay;
    Double amtPaid;
    Double otherDiff;
    String diffDetail;
    Date createdDate;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "party_id", referencedColumnName = "id", insertable = true, updatable = true)
    Party party;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "createdBy", referencedColumnName = "id", insertable = true, updatable = true)
    UserData createdBy;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "updatedBy", referencedColumnName = "id", insertable = true, updatable = true)
    UserData updatedBy;

    Date updatedDate;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<PaymentData> paymentData;


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }


    public PaymentMast(AddPaymentMast paymentMast,Party party,UserData createdBy,UserData updatedBy) {

        this.party = party;
        this.totalBill = paymentMast.getTotalBill();
        this.GstAmt = paymentMast.getGstAmt();
        this.rdAmt = paymentMast.getRdAmt();
        this.rdDetail = paymentMast.getRdDetail();
        this.cdAmt = paymentMast.getCdAmt();
        this.cdDetail = paymentMast.getCdDetail();
        this.amtToPay = paymentMast.getAmtToPay();
        this.amtPaid = paymentMast.getAmtPaid();
        this.otherDiff = paymentMast.getOtherDiff();
        this.diffDetail = paymentMast.getDiffDetail();
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.tdsAmt = paymentMast.getTdsAmt();
        this.tdsDetail = paymentMast.getTdsDetail();

    }

    public PaymentMast(PaymentMast paymentMast) {
        this.id = paymentMast.getId();
        this.diffDetail = paymentMast.getDiffDetail();
        this.rdDetail = paymentMast.getRdDetail();
        this.createdDate = paymentMast.getCreatedDate();
        this.party = paymentMast.getParty();
        this.totalBill = paymentMast.getTotalBill();
        this.GstAmt = paymentMast.getGstAmt();
        this.rdAmt = paymentMast.getRdAmt();
        this.rdDetail = paymentMast.getRdDetail();
        this.cdAmt = paymentMast.getCdAmt();
        this.cdDetail = paymentMast.getCdDetail();
        this.amtToPay = paymentMast.getAmtToPay();
        this.amtPaid = paymentMast.getAmtPaid();
        this.otherDiff = paymentMast.getOtherDiff();
        this.diffDetail = paymentMast.getDiffDetail();
        this.createdBy = paymentMast.getCreatedBy();
        this.updatedBy = paymentMast.getUpdatedBy();
    }

}
