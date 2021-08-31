package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddStockBatch {
    Long id;
    String stockInType;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id", referencedColumnName = "id", insertable = true, updatable = true)    
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
    Date createdDate;
    Date receiveDate;
    Date updatedDate;
    String remark;
    Double wtPer100m;
    List<BatchData> batchData;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id", referencedColumnName = "id", insertable = true, updatable = true)    
    Quality quality;

}
