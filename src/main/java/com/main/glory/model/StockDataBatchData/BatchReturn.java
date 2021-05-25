package com.main.glory.model.StockDataBatchData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BatchReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long stockId;
    Date createdDate;
    Date updatedDate;
    Long createdBy;
    Long updatedBy;
    Long batchEntryId;
    Long partyId;
    Long qualityEntryId;
    Double mtr;
    Long chlNo;

    public BatchReturn(StockMast stockMast, BatchData batchDataExist,Long chlNo) {
        this.stockId = stockMast.getId();
        this.partyId = stockMast.getPartyId();
        this.qualityEntryId = stockMast.getQualityId();
        this.mtr = batchDataExist.getMtr();
        this.batchEntryId = batchDataExist.getId();
        this.chlNo= chlNo;

    }

    public BatchReturn(Long qualityEntryId, Long partyId, BatchData batchDataExist,Long chlNo) {
        this.stockId = batchDataExist.getControlId();
        this.partyId = partyId;
        this.qualityEntryId = qualityEntryId;
        this.mtr = batchDataExist.getMtr();
        this.batchEntryId = batchDataExist.getId();
        this.chlNo = chlNo;
    }

    @PrePersist
    public void create()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    public void update()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
