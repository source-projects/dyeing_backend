package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchReturnBody {
    Long id;
    Long createdBy;
    Long updatedBy;
    List<BatchData> batchDataList;

}
