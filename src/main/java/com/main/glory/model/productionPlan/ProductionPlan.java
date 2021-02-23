package com.main.glory.model.productionPlan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.jet.JetData;
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
    Long stockId;
    Long partyId;
    Long qualityEntryId;
    Long shadeId;
    Boolean status=false;

    public ProductionPlan(ProductionPlan productionPlan) {
        this.shadeId=productionPlan.getShadeId();
        this.qualityEntryId=productionPlan.getQualityEntryId();
        this.partyId=productionPlan.getPartyId();
        this.batchId=productionPlan.getBatchId();
        this.stockId=productionPlan.getStockId();
        this.status=productionPlan.getStatus();
        this.id=productionPlan.getId();

    }
}
