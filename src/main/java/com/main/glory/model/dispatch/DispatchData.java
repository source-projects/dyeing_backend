package com.main.glory.model.dispatch;

import com.main.glory.model.StockDataBatchData.BatchData;
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

    public DispatchData(BatchData batchData) {
        this.batchEntryId=batchData.getId();
        this.batchId=batchData.getBatchId();
        this.stockId=batchData.getControlId();
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }


}
