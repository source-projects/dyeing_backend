package com.main.glory.model.dyeingSlip;

import com.main.glory.model.dyeingSlip.request.AddAdditionDyeingSlipModel;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.productionPlan.request.AddDirectBatchToJet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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



    @OneToMany(cascade = CascadeType.ALL)
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
