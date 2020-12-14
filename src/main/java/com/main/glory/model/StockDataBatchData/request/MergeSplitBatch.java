package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
public class MergeSplitBatch {
    String batchId;
    Long controlId;
    Boolean isSplit;
    List<BatchData> batchDataList;
}
