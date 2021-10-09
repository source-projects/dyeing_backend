package com.main.glory.model.StockDataBatchData;

import com.main.glory.model.StockDataBatchData.request.BatchReturnBody;
import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BatchReturnMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Date createdDate;
    Date updatedDate;
    Long createdBy;
    Long updatedBy;
    Long chlNo;
    Date challanDate;
    Long partyId;
    String partyName;
    String address;
    String partyCode;
    String gst;
    String broker;
    String tempoNo;
    String pchallanNo;
    String diffPartyName;
    String diffPartyAddress;
    String diffGst;
    @ColumnDefault("false")
    Boolean diffDeliveryParty;


    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<BatchReturnData> batchReturnData;

    public BatchReturnMast(Party party, BatchReturnBody record, Long latestChlNo,StockMast stockMast) {
        this.partyId = party.getId();
        this.gst = party.getGSTIN()==null?null:party.getGSTIN();
        this.partyCode = party.getPartyCode();
        this.partyName = party.getPartyName();
        this.address = party.getPartyAddress1();
        this.createdBy =record.getCreatedBy()==null?null:record.getCreatedBy();
        this.challanDate = record.getChallanDate()==null?null:record.getChallanDate();
        this.chlNo =latestChlNo;
        this.broker = record.getBroker();
        this.tempoNo = record.getTempoNo();
        this.pchallanNo = stockMast.getChlNo();
        this.diffPartyName = party.getPartyName();
        this.diffPartyAddress= party.getPartyAddress1();
        this.diffGst = party.getGSTIN()==null?null:party.getGSTIN();
        this.diffDeliveryParty = false;


    }

    @PrePersist
    public void create()
    {
        this.challanDate = new Date(System.currentTimeMillis());
        this.createdDate=new Date(System.currentTimeMillis());
    }

    @PreUpdate
    public void update()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
