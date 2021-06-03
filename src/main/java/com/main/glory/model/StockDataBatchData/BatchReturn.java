package com.main.glory.model.StockDataBatchData;

import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.response.QualityWithQualityNameParty;
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
    String partyName;
    String address;
    Long qualityEntryId;
    String qualityName;
    String qualityId;
    Double mtr;
    Long chlNo;
    String batchId;
    Date challanDate;

    public BatchReturn(StockMast stockMast, BatchData batchDataExist, Long chlNo, Date challanDate, Party party, QualityWithQualityNameParty quality) {
        this.stockId = stockMast.getId();
        this.partyId = stockMast.getPartyId();
        this.address = party.getPartyAddress1();
        this.qualityEntryId = stockMast.getQualityId();
        this.qualityName = quality.getQualityName();
        this.qualityId = quality.getQualityId();
        this.mtr = batchDataExist.getMtr();
        this.batchEntryId = batchDataExist.getId();
        this.chlNo= chlNo;
        this.batchId = batchDataExist.getBatchId();
        this.challanDate = challanDate;

    }

    public BatchReturn(Long qualityEntryId, Long partyId, BatchData batchDataExist, Long chlNo, Date challanDate, Party party, QualityWithQualityNameParty quality) {
        this.challanDate = challanDate;
        this.stockId = batchDataExist.getControlId();
        this.partyId = partyId;
        this.address = party.getPartyAddress1();
        this.qualityEntryId = qualityEntryId;
        this.qualityId = quality.getQualityId();
        this.qualityName = quality.getQualityName();
        this.mtr = batchDataExist.getMtr();
        this.batchEntryId = batchDataExist.getId();
        this.chlNo = chlNo;
        this.batchId = batchDataExist.getBatchId();
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
