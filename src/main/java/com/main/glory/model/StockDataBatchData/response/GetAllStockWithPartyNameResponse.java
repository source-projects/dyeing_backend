package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class GetAllStockWithPartyNameResponse extends StockMast{



    List<GetAllBatchResponse> batchList;
    String partyName;
    String qualityName;


    public GetAllStockWithPartyNameResponse(StockMast stockMast, String partyName,String qualityName) {
        super(stockMast);
        this.partyName = partyName;
        this.qualityName=qualityName;
    }

    public GetAllStockWithPartyNameResponse(GetAllStockWithPartyNameResponse batchData, List<GetAllBatchResponse> batchDataList,String qualityName) {
        this.setId(batchData.getId());
        this.setStockInType(batchData.getStockInType());
        this.setPartyId(batchData.getPartyId());
        this.setPartyName(batchData.partyName);
        this.setBillNo(batchData.getBillNo());
        this.setBillDate(batchData.getBillDate());
        this.setChlNo(batchData.getChlNo());
        this.setChlDate(batchData.getChlDate());
        this.setUnit(batchData.getUnit());
        this.setUpdatedBy(batchData.getUpdatedBy());
        this.setCreatedBy(batchData.getCreatedBy());
        this.setUserHeadId(batchData.getUserHeadId());
        this.setIsProductionPlanned(batchData.getIsProductionPlanned());
        this.setCreatedDate(batchData.getCreatedDate());
        this.setUpdatedDate(batchData.getUpdatedDate());
        this.setRemark(batchData.getRemark());
        this.setBatchList(batchDataList);
        this.setQualityName(qualityName);
        this.setBatchData(batchData.getBatchData());
    }


}
