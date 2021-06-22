package com.main.glory.model.StockDataBatchData;

import com.main.glory.model.quality.response.QualityWithQualityNameParty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Service
@Entity
public class BatchReturnData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long stockId;
    Long batchEntryId;
    String batchId;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    Double mtr;
    Double wt;
    Long controlId;

    public BatchReturnData(Optional<QualityWithQualityNameParty> qualityWithQualityNameParty, BatchData e) {
        this.stockId = e.getControlId();
        this.batchEntryId =e.getId();
        this.batchId = e.getBatchId();
        this.qualityEntryId = qualityWithQualityNameParty.get().getId();
        this.qualityId = qualityWithQualityNameParty.get().getQualityId();
        this.qualityName = qualityWithQualityNameParty.get().getQualityName();
        this.mtr = e.getMtr();
        this.wt = e.getWt();
    }
}
