package com.main.glory.model.StockDataBatchData;


import com.sun.istack.NotNull;
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
    @NotNull
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
    String pchallanRef;
    @ColumnDefault("false")
    Boolean avoidCommission;



   /* @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "batchEntryId", referencedColumnName = "id")
    List<DispatchData> dispatchData;*/

    public BatchData(BatchData other) {
        this.id=other.id==null?null:other.id;
        this.mtr=other.mtr;
        this.wt=other.wt==null?null:other.wt;
        this.batchId=other.batchId==null?null:other.getBatchId();
        this.controlId=other.controlId==null?0:other.getControlId();
        this.isProductionPlanned = other.getIsProductionPlanned()==null?false:other.getIsProductionPlanned();
        this.isExtra=other.isExtra==null?false:other.getIsExtra();
        this.sequenceId=other.sequenceId==null?0l:other.getSequenceId();
        this.finishMtr=other.finishMtr==null?0:other.getFinishMtr();
        this.isBillGenrated=other.isBillGenrated==null?false:other.getIsBillGenrated();
        this.isFinishMtrSave = other.isFinishMtrSave==null?false:other.getIsFinishMtrSave();
        this.pchallanRef = other.getPchallanRef()==null?null:other.getPchallanRef();
        this.avoidCommission = other.getAvoidCommission()==null?false:other.getAvoidCommission();

    }


}
