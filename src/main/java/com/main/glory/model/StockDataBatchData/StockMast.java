package com.main.glory.model.StockDataBatchData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.request.AddStockBatch;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StockMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String stockInType;
    Long partyId;
    String billNo;
    Date billDate;
    Long chlNo;
    Date chlDate;
    String unit;
    Long updatedBy;
    Long createdBy;
    Long userHeadId;
    Boolean isProductionPlanned;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    Date receiveDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;
    String remark;
    Double wtPer100m;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<BatchData> batchData;
    Long qualityId;

   /* @PreRemove
    public void checkBatches() throws Exception {
        if(!this.batchData.isEmpty())
            throw new Exception("data can't be deleted because related to other record");
    }*/

    public StockMast(StockMast sm) {
        this.id = sm.id;
        this.stockInType = sm.stockInType;
        this.createdDate = sm.createdDate;
        this.updatedDate = sm.updatedDate;
        this.batchData = sm.batchData;
        this.partyId = sm.partyId;
        this.qualityId = sm.qualityId;
        this.chlDate = sm.chlDate;
        this.chlNo = sm.chlNo;
        this.unit = sm.unit;
        this.isProductionPlanned = sm.isProductionPlanned;
        this.createdBy = sm.createdBy;
        this.updatedBy = sm.updatedBy;
        this.userHeadId= sm.getUserHeadId();
        this.receiveDate=sm.receiveDate;
    }

    public StockMast(AddStockBatch sm) {
        this.id = sm.getId();
        this.stockInType = sm.getStockInType();
        this.createdDate = sm.getCreatedDate();
        this.updatedDate = sm.getUpdatedDate();
        //this.batchData = sm.getBatchData();
        this.partyId = sm.getPartyId();
        this.qualityId = sm.getQualityId();
        this.chlDate = sm.getChlDate();
        this.chlNo = sm.getChlNo();
        this.unit = sm.getUnit();
        this.isProductionPlanned = sm.getIsProductionPlanned();
        this.createdBy = sm.getCreatedBy();
        this.updatedBy = sm.getUpdatedBy();
        this.userHeadId= sm.getUserHeadId();
        this.receiveDate=sm.getReceiveDate();
        //this.batchData =sm.getBatchData();

    }


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }


}
