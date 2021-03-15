package com.main.glory.model.dispatch;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import lombok.*;

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
    Long batchEntryId;
    String batchId;
    Long stockId;
    String invoiceNo;
    Boolean isSendToParty=false;
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    Long qualityEntryId;
    Long shadeId;
    Double qualityRate;
    Double shadeRate;
    @Column(columnDefinition = "varchar(255) default 'meter'")
    String billingUnit;


    public DispatchData(BatchData batchData) {
        this.batchEntryId=batchData.getId();
        this.batchId=batchData.getBatchId();
        this.stockId=batchData.getControlId();

    }

    public DispatchData(BatchData batchData, ShadeMast shadeMast, Quality quality) {
        this.batchEntryId=batchData.getId();
        this.batchId=batchData.getBatchId();
        this.stockId=batchData.getControlId();
        this.qualityEntryId=quality.getId();
        this.qualityRate=quality.getRate();
        this.shadeId=shadeMast.getId();
        //this.shadeRate=shadeMast.getExtraRate();
    }



    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }


}
