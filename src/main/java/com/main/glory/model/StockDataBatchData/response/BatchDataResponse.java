package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BatchDataResponse {

    Long id;
    Double mtr;
    Double wt;
    String batchId;
    Long controlId;
    Boolean isProductionPlanned;


}
