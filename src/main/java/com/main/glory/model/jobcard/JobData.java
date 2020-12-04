package com.main.glory.model.jobcard;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.program.ProgramRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class JobData {
    @Id
    @GeneratedValue
    Long id;
    Long stockId;
    String batchId;
    Long batchEntryId;
    Double mtr;
    Double finishMtr;
    Long controlId;
    Long finishSeqId;

    public JobData(BatchData batchData) {
        this.stockId=batchData.getControlId();
        this.batchEntryId=batchData.getId();
        this.batchId=batchData.getBatchId();
        this.finishMtr=00.0;
        this.finishSeqId=0l;
        this.mtr=batchData.getMtr();

    }
}
