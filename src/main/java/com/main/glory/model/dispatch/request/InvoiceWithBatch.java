package com.main.glory.model.dispatch.request;

import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceWithBatch {
    String invoiceNo;
    List<BatchWithTotalMTRandFinishMTR> batchList;

}
