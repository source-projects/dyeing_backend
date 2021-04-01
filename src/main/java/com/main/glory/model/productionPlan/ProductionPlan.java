package com.main.glory.model.productionPlan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.productionPlan.request.AddDirectBatchToJet;
import com.main.glory.model.productionPlan.request.AddProductionWithJet;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class ProductionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String batchId;
    Long shadeId;
    /*@Column(columnDefinition = "varchar(255) default 'Not mention'")
    String colorName;*/
    @Column(columnDefinition = "boolean default false")
    Boolean isDirect=false;
    @Column(columnDefinition = "boolean default false")
    Boolean isMergeBatchId;
    Boolean status=false;

    public ProductionPlan(ProductionPlan productionPlan) {
        this.shadeId=productionPlan.getShadeId();
       /* this.qualityEntryId=productionPlan.getQualityEntryId();
        this.partyId=productionPlan.getPartyId();*/
        this.batchId=productionPlan.getBatchId();
       // this.stockId=productionPlan.getStockId();
        this.status=productionPlan.getStatus();
        this.id=productionPlan.getId();
        this.isMergeBatchId=productionPlan.getIsMergeBatchId();
        this.isDirect=productionPlan.getIsDirect();

    }
    public ProductionPlan(GetAllProductionWithShadeData productionPlan) {
        this.shadeId=productionPlan.getShadeId();
       /* this.qualityEntryId=productionPlan.getQualityEntryId();
        this.partyId=productionPlan.getPartyId();*/
        this.batchId=productionPlan.getBatchId();
        // this.stockId=productionPlan.getStockId();
        this.status=productionPlan.getStatus();
        this.id=productionPlan.getId();
        this.isMergeBatchId=productionPlan.getIsMergeBatchId();
        this.isDirect=productionPlan.getIsDirect();

    }

    public ProductionPlan(AddProductionWithJet productionPlan) {
        this.id=productionPlan.getProductionId();
        this.batchId=productionPlan.getBatchId();
        /*this.stockId=productionPlan.getStockId();
        this.partyId=productionPlan.getPartyId();
        this.qualityEntryId=productionPlan.getQualityEntryId();*/
        this.shadeId=productionPlan.getShadeId();
        this.status = false;
    }

    public ProductionPlan(AddDirectBatchToJet record) {
       /* this.partyId=record.getPartyId();
        this.qualityEntryId = record.getQualityEntryId();*/
        this.batchId = record.getBatchId();
       // this.stockId= record.getStockId();
        this.isDirect = true;
        this.status=true;
        this.shadeId=(record.getShadeId()!=null)?record.getShadeId():null;

    }
}
