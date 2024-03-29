package com.main.glory.model.dispatch;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DispatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name="batchEntryId", referencedColumnName = "id", insertable = true, updatable = true)
    BatchData batchData;
    String batchId;
    Long stockId;
    Long controlId;
    String invoiceNo;
    @Column(columnDefinition = "boolean default false")
    Boolean isSendToParty;
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name="qualityEntryId", referencedColumnName = "id", insertable = true, updatable = true)
    Quality quality;
    Long shadeId;
    Double qualityRate;
    Double shadeRate;
    @Column(columnDefinition = "varchar(255) default 'meter'")
    String billingUnit;
    Double wtPer100m;
    String pchallanRef;
    String inwardUnit; //From quality take unit


    public DispatchData(BatchData batchData) {
        this.batchData=batchData;
        this.batchId=batchData.getBatchId();
        this.stockId=batchData.getControlId();
        this.pchallanRef = batchData.getPchallanRef();

    }

    public DispatchData(BatchData batchData, ShadeMast shadeMast, Quality quality, StockMast stockMast) {
        this.batchData=batchData;
        this.batchId=batchData.getBatchId();
        this.stockId=batchData.getControlId();
        this.quality=quality;
        this.qualityRate=quality.getRate();
        this.shadeId=shadeMast==null?null:shadeMast.getId();
        this.billingUnit = quality.getBillingUnit();
        this.inwardUnit =quality.getUnit();
        this.wtPer100m = stockMast.getWtPer100m();
        this.pchallanRef = batchData.getPchallanRef();
        this.shadeRate=shadeMast !=null ? shadeMast.getExtraRate():null;
    }
    public DispatchData(BatchData batchData,  Quality quality,StockMast stockMast) {
        this.batchData=batchData;
        this.batchId=batchData.getBatchId();
        this.stockId=batchData.getControlId();
        this.quality=quality;
        this.qualityRate=quality.getRate();
        this.inwardUnit = quality.getUnit();
        this.billingUnit = quality.getBillingUnit();
        this.wtPer100m = stockMast.getWtPer100m();
        this.pchallanRef = batchData.getPchallanRef();
        //this.shadeId=shadeMast.getId()==null?null:shadeMast.getId();
        //this.shadeRate=shadeMast.getExtraRate();
    }



    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }


}
