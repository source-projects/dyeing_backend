package com.main.glory.model.party.request;


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

    private String partyName;
    private String partyAddress1;
    private String partyAddress2;
    private String contactNo;
    private Integer pincode;
    private String city;
    private String state;
    private String GSTIN;
    private String mailId;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedById", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData updatedBy;    
    private Boolean debtor;
    private Boolean creditor;
    private Boolean internalTransfer;
    private String partyType;
    private String paymentTerms;
    private Double percentageDiscount;
    private Double gstPercentage;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;
    private String partyCode;
    Long paymentDays;
    Double creditLimit;
    Double discount;
}
