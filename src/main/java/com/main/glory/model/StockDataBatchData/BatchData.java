package com.main.glory.model.StockDataBatchData;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "batchData")
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Double mtr;
    Double wt;
    String batchId;
    Long controlId;
    Boolean isProductionPlanned=false;
    Boolean isExtra=false;
    Long sequenceId=0l;
    Double finishMtr=0.0;
    Boolean isBillGenrated=false;

    public BatchData(BatchData other) {
        this.id=other.id;
        this.mtr=other.mtr;
        this.wt=other.wt;
        this.batchId=other.batchId;
        this.controlId=other.controlId;
        this.isExtra=other.isExtra;
        this.sequenceId=other.sequenceId;
        this.finishMtr=other.finishMtr;
        this.isBillGenrated=other.isBillGenrated;

    }


}
