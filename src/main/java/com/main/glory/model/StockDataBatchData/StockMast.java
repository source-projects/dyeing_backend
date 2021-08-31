package com.main.glory.model.StockDataBatchData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.request.AddStockBatch;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
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

    /*
    *
    * Don't change anything otherwise it will ruin
    *
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String stockInType;
    
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="party_id", referencedColumnName = "id", insertable = true, updatable = true)    
    Party party;
    
    String billNo;
    Date billDate;
    String chlNo;
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

    // @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<BatchData> batchData;

    // @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="quality_id", referencedColumnName = "id", insertable = true, updatable = true)    
    Quality quality;

   /* @PreRemove
    public void checkBatches() throws Exception {
        if(!this.batchData.isEmpty())
            throw new Exception("data can't be deleted because related to other record");
    }*/

    public StockMast(StockMast sm) {
        this.id = sm.id;
        this.stockInType = sm.stockInType;
        this.party = sm.party;
        this.billNo=sm.billNo;
        this.billDate = sm.billDate;
        this.chlDate = sm.chlDate;
        this.chlNo = sm.chlNo;
        this.createdDate = sm.createdDate;
        this.updatedDate = sm.updatedDate;
        this.batchData = sm.batchData;
        this.quality = sm.quality;
        this.unit = sm.unit;
        this.isProductionPlanned = sm.isProductionPlanned;
        this.createdBy = sm.createdBy;
        this.updatedBy = sm.updatedBy;
        this.userHeadId= sm.getUserHeadId();
        this.receiveDate=sm.receiveDate;
        this.remark =sm.getRemark();
        this.wtPer100m=sm.getWtPer100m();
    }

    public StockMast(AddStockBatch sm) {
        this.id = sm.getId();
        this.stockInType = sm.getStockInType();
        this.party = sm.getParty();
        this.billDate = sm.getBillDate()==null?null:sm.getBillDate();
        this.billNo = sm.getBillNo()==null?null:sm.getBillNo();
        this.chlDate = sm.getChlDate();
        this.chlNo = sm.getChlNo();
        this.unit = sm.getUnit();
        this.createdBy = sm.getCreatedBy();
        this.updatedBy = sm.getUpdatedBy();
        this.userHeadId= sm.getUserHeadId();
        this.isProductionPlanned = sm.getIsProductionPlanned();
        this.createdDate = sm.getCreatedDate();
        this.updatedDate = sm.getUpdatedDate();
        this.receiveDate=sm.getReceiveDate();
        this.remark = sm.getRemark()==null?null:sm.getRemark();
        this.wtPer100m=sm.getWtPer100m();
        //this.batchData = sm.getBatchData();
        this.quality = sm.getQuality();

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
