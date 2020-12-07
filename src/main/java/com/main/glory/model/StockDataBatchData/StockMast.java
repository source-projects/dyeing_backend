package com.main.glory.model.StockDataBatchData;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ApiModelProperty(hidden = true)
    Date updatedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<BatchData> batchData;


    Long qualityId;

    public StockMast(StockMast sm) {
        this.id = sm.id;
        this.stockInType = sm.stockInType;
        this.createdDate = sm.createdDate;
        this.updatedDate = sm.updatedDate;
        this.batchData = sm.batchData;
        this.partyId = sm.partyId;
        this.qualityId = sm.qualityId;
        this.billDate = sm.billDate;
        this.billNo = sm.billNo;
        this.chlDate = sm.chlDate;
        this.chlNo = sm.chlNo;
        this.unit = sm.unit;
        this.isProductionPlanned = sm.isProductionPlanned;
        this.createdBy = sm.createdBy;
        this.updatedBy = sm.updatedBy;
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
