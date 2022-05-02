package com.main.glory.model.party.request;


import com.main.glory.model.party.Party;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.main.glory.model.user.UserData;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddParty {
    Long id;
    private String partyName;
    private String partyAddress1;
    private String partyAddress2;
    private String contactNo;
    private Integer pincode;
    private String city;
    private String state;
    private String GSTIN;
    private String mailId;
    private Long createdBy;
    private Long updatedBy;    
    private Boolean debtor;
    private Boolean creditor;
    private Boolean internalTransfer;
    private String partyType;
    private String paymentTerms;
    private Double percentageDiscount;
    private Double gstPercentage;
    private Date createdDate;
    private Date updatedDate;
    private Long userHeadId;
    private String partyCode;
    Long paymentDays;
    Double creditLimit;
    Double discount;
    Boolean blockBilling;

    public AddParty(Party party) {
        this.id = party.getId();
        this.partyName = party.getPartyName();
        this.partyAddress1 = party.getPartyAddress1();
        this.partyAddress2 = party.getPartyAddress2();
        this.contactNo = party.getContactNo();
        this.pincode = party.getPincode();
        this.city = party.getCity();
        this.state = party.getState();
        this.GSTIN = party.getGSTIN();
        this.mailId = party.getMailId();
        this.createdBy = party.getCreatedBy() == null ? null:party.getCreatedBy().getId();
        this.updatedBy = party.getUpdatedBy()==null ? null:party.getUpdatedBy().getId();
        this.debtor = party.getDebtor();
        this.creditor = party.getCreditor();
        this.internalTransfer = party.getInternalTransfer();
        this.partyType = party.getPartyType();
        this.paymentTerms = party.getPaymentTerms();
        this.percentageDiscount = party.getPercentageDiscount();
        this.gstPercentage = party.getGstPercentage();
        this.createdDate = party.getCreatedDate();
        this.updatedDate = party.getUpdatedDate()==null?null:party.getUpdatedDate();
        this.userHeadId = party.getUserHeadData().getId();
        this.partyCode = party.getPartyCode();
        this.paymentDays = party.getPaymentDays();
        this.creditLimit = party.getCreditLimit();
        this.blockBilling = party.getBlockBilling();
        //this.discount = party.getPercentageDiscount();
    }
}
