package com.main.glory.model.StockDataBatchData;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("false")
    Boolean isProductionPlanned;
    @ColumnDefault("false")
    Boolean isExtra;
    @ColumnDefault("0")
    Long sequenceId;
    Double finishMtr;
    @ColumnDefault("false")
    Boolean isBillGenrated;
    @ColumnDefault("false")
    Boolean isFinishMtrSave;
    String mergeBatchId;

   /* @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "batchEntryId", referencedColumnName = "id")
    List<DispatchData> dispatchData;*/

    public BatchData(BatchData other) {
        this.id=other.id==null?0:other.id;
        this.mtr=other.mtr;
        this.wt=other.wt;
        this.batchId=other.batchId;
        this.controlId=other.controlId==null?0:other.getControlId();
        this.isProductionPlanned = other.getIsProductionPlanned()==null?false:other.getIsProductionPlanned();
        this.isExtra=other.isExtra==null?false:other.getIsExtra();
        this.sequenceId=other.sequenceId==null?0l:other.getSequenceId();
        this.finishMtr=other.finishMtr==null?0:other.getFinishMtr();
        this.isBillGenrated=other.isBillGenrated==null?false:other.getIsBillGenrated();
        this.isFinishMtrSave = other.isFinishMtrSave==null?false:other.getIsFinishMtrSave();


    }


}
