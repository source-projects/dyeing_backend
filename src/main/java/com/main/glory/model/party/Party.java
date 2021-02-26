package com.main.glory.model.party;

import com.main.glory.model.party.request.AddParty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="party")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;
    private String partyName;
    private String partyAddress1;
    private String partyAddress2;
    private String contactNo;
    private Integer pincode;
    private String city;
    private String state;
    @Column(name = "GSTIN")
    private String GSTIN;
    private String mailId;
    private Date createdDate;
    private Long createdBy;
    private Date updatedDate;
    private Long updatedBy;
    private Boolean debtor;
    private Boolean creditor;
    private Boolean internalTransfer;
    private String partyType;
    private String paymentTerms;
    private Double percentageDiscount;
    private Double gstPercentage;
    private Long userHeadId;
    private String partyCode;


    public Party(Party party) {
        this.id=party.getId();
        this.partyName=party.getPartyName();
        this.partyAddress1=party.getPartyAddress1();
        this.partyAddress2=party.getPartyAddress2();
        this.contactNo=party.getContactNo();
        this.pincode=party.getPincode();
        this.city = party.getCity();
        this.state=party.getState();
        this.GSTIN=party.getGSTIN();
        this.mailId=party.getMailId();
        this.createdDate = party.getCreatedDate();
        this.createdBy=party.getCreatedBy();
        this.updatedDate=party.getUpdatedDate();
        this.updatedBy=party.getUpdatedBy();
        this.debtor=party.getDebtor();
        this.creditor=party.getCreditor();
        this.internalTransfer=party.getInternalTransfer();
        this.partyType=party.getPartyType();
        this.paymentTerms=party.getPaymentTerms();
        this.percentageDiscount=party.getPercentageDiscount();
        this.gstPercentage = party.getGstPercentage();
        this.userHeadId=party.getUserHeadId();
        this.partyCode=party.getPartyCode();
    }


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }


    public Party(AddParty addParty)
    {
        this.partyName= addParty.getPartyName();
        this.partyAddress1=addParty.getPartyAddress1();
        this.partyAddress2=addParty.getPartyAddress2();
        this.contactNo=addParty.getContactNo();
        this.pincode=addParty.getPincode();
        this.city=addParty.getCity();
        this.state=addParty.getState();
        this.GSTIN=addParty.getGSTIN();
        this.mailId=addParty.getMailId();
        this.createdBy=addParty.getCreatedBy();
        this.updatedBy=addParty.getUpdatedBy();
        this.debtor=addParty.getDebtor();
        this.creditor=addParty.getCreditor();
        this.internalTransfer=addParty.getInternalTransfer();
        this.partyType=addParty.getPartyType();
        this.paymentTerms=addParty.getPaymentTerms();
        this.percentageDiscount=addParty.getPercentageDiscount();
        this.gstPercentage=addParty.getGstPercentage();
        this.userHeadId=addParty.getUserHeadId();
        this.partyCode=addParty.getPartyCode();
    }

}
