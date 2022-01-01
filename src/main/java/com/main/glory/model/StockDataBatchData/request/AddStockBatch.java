package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
    Long partyId;
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
    Long qualityId;
    Boolean isRfInvoice;

    public AddStockBatch(StockMast sm) {
            this.id = sm.getId();
            this.stockInType = sm.getStockInType();
            this.partyId = sm.getParty().getId();
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
            this.batchData = sm.getBatchData();
            this.qualityId =sm.getQuality().getId();
            this.batchData=sm.getBatchData();


    }
}
