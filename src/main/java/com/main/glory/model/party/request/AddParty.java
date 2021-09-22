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
}
