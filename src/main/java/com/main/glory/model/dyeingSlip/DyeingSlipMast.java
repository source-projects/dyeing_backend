package com.main.glory.model.dyeingSlip;

import com.main.glory.model.dyeingSlip.request.AddAdditionDyeingSlipModel;
import com.main.glory.model.dyeingSlip.request.SlipFormatData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.productionPlan.request.AddDirectBatchToJet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class DyeingSlipMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long userHeadId;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;
    Long stockId;
    Long jetId;
    Long productionId;
    String batchId;
    @ColumnDefault("0")
    Long dyeingProcessMastId;



    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<DyeingSlipData> dyeingSlipDataList;

    Long approvedId;

    public DyeingSlipMast(DyeingSlipMast dyeingSlipMastExist) {
        this.id=dyeingSlipMastExist.id;
        this.userHeadId=dyeingSlipMastExist.userHeadId;
        this.createdBy=dyeingSlipMastExist.createdBy;
        this.updatedBy=dyeingSlipMastExist.updatedBy;
        this.createdDate=dyeingSlipMastExist.createdDate;
        this.updatedDate=dyeingSlipMastExist.updatedDate;
        this.stockId=dyeingSlipMastExist.stockId;
        this.jetId=dyeingSlipMastExist.jetId;
        this.productionId=dyeingSlipMastExist.productionId;
        this.batchId=dyeingSlipMastExist.batchId;
        this.dyeingProcessMastId = dyeingSlipMastExist.getDyeingProcessMastId()==null?null:dyeingSlipMastExist.getDyeingProcessMastId();
    }
    public DyeingSlipMast(SlipFormatData dyeingSlipMastExist) {
        this.id=dyeingSlipMastExist.getId();
        this.userHeadId=dyeingSlipMastExist.getUserHeadId();
        this.createdBy=dyeingSlipMastExist.getCreatedBy();
        this.updatedBy=dyeingSlipMastExist.getUpdatedBy();
        this.createdDate=dyeingSlipMastExist.getCreatedDate();
        this.updatedDate=dyeingSlipMastExist.getUpdatedDate();
        this.stockId=dyeingSlipMastExist.getStockId();
        this.jetId=dyeingSlipMastExist.getJetId();
        this.productionId=dyeingSlipMastExist.getProductionId();
        this.batchId=dyeingSlipMastExist.getBatchId();
        this.dyeingProcessMastId = dyeingSlipMastExist.getDyeingProcessMastId()==null?null:dyeingSlipMastExist.getDyeingProcessMastId();
    }



    /*public DyeingSlipMast(AddAdditionDyeingSlipModel addAdditionDyeingSlipModel) {

        this.createdBy = addAdditionDyeingSlipModel.getCreatedBy();
        this.updatedBy = addAdditionDyeingSlipModel.getUpdatedBy();
        this.userHeadId = addAdditionDyeingSlipModel.getUserHeadId();

        this.productionId = addAdditionDyeingSlipModel.getProductionId();
        this.batchId = addAdditionDyeingSlipModel.getBatchId();
    }*/

    public DyeingSlipMast(AddDirectBatchToJet record, ProductionPlan x) {
        this.createdBy = record.getCreatedBy();
        this.updatedBy = record.getUpdatedBy();
        //this.userHeadId = record.getUserHeadId();

        this.jetId = record.getJetId();
        this.productionId = x.getId();
        this.batchId = x.getBatchId();
        //this.stockId = x.getStockId();
    }


    @PrePersist
    void onCreate(){this.createdDate=new Date(System.currentTimeMillis());}


}
