package com.main.glory.model.StockDataBatchData.response;

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
public class BatchToPartyQualityWithGr extends BatchToPartyAndQuality{
    List<BatchData> batchDataList;
}
