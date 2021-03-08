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
    String partyName;
    String qualityName;


    public GetAllStockWithPartyNameResponse(StockMast stockMast, String partyName,String qualityName) {
        super(stockMast);
        this.partyName = partyName;
        this.qualityName=qualityName;
    }

}
