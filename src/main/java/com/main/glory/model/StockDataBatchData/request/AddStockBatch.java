package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
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
    Date createdDate;
    Date receiveDate;
    Date updatedDate;
    String remark;
    Double wtPer100m;
    List<BatchData> batchData;
    Long qualityId;

}
