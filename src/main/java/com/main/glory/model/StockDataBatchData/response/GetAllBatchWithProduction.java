package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class GetAllBatchWithProduction extends GetAllBatch{

    Long productionId;


    public GetAllBatchWithProduction(GetAllBatch getAllBatch, Long id) {
        super(getAllBatch);
        this.productionId=id;

    }
}
