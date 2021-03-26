package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.MergeBatchId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateMergeBatch {
    String mergeBatchId;
    List<BatchData> batchDataList;

    public CreateMergeBatch(MergeBatchId record, List<BatchData> batchData) {
        this.mergeBatchId = record.getMergeBatchId();
        this.batchDataList = batchData;
    }
}
