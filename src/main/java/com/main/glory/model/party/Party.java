package com.main.glory.model.party;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.user.UserData;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

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
    @ColumnDefault("0")
    Long paymentDays;
    @ColumnDefault("0.0")
    Double creditLimit;
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
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedById", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData updatedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;

    
    private Boolean debtor;
    private Date updatedDate;
    private Boolean creditor;
    private Boolean internalTransfer;
    private String partyType;
    private String paymentTerms;
    @ColumnDefault("0.0")
    private Double percentageDiscount;
    private Double gstPercentage;
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
        this.userHeadData=party.getUserHeadData();
        this.partyCode=party.getPartyCode();
        this.creditLimit = party.getCreditLimit();
        this.paymentDays = party.getPaymentDays();
    }


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }


    public Party(AddParty addParty,UserData userHeadData,UserData createdBy,UserData updatedBy)
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
        this.debtor=addParty.getDebtor();
        this.creditor=addParty.getCreditor();
        this.internalTransfer=addParty.getInternalTransfer();
        this.partyType=addParty.getPartyType();
        this.paymentTerms=addParty.getPaymentTerms();
        this.percentageDiscount=addParty.getPercentageDiscount();
        this.gstPercentage=addParty.getGstPercentage();
        this.partyCode=addParty.getPartyCode();
        this.paymentDays=addParty.getPaymentDays();
        this.creditLimit=addParty.getCreditLimit();

        
        this.userHeadData=userHeadData;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;


    }

}
