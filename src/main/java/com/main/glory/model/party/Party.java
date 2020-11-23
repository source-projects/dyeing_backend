package com.main.glory.model.party;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.program.Program;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "entry_id", updatable = false, nullable = false)
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
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
    private Boolean debtor;
    private Boolean creditor;
    private Boolean internalTransfer;
    private String partyType;
    private String paymentTerms;
    private Double percentageDiscount;
    private Double gstPercentage;
    private Integer userHeadId;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "partyId", referencedColumnName = "entry_id")
    private List<Program> program;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "partyId", referencedColumnName = "entry_id")
    private List<StockMast> stockMasts;

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
        this.createdBy=null;
        this.updatedBy=null;
        this.debtor=addParty.getDebtor();
        this.creditor=addParty.getCreditor();
        this.internalTransfer=addParty.getInternalTransfer();
        this.partyType=addParty.getPartyType();
        this.paymentTerms=addParty.getPaymentTerms();
        this.percentageDiscount=addParty.getPercentageDiscount();
        this.userHeadId=addParty.getUserHeadId();

    }

}
