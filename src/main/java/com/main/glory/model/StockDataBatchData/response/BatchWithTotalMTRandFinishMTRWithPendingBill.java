package com.main.glory.model.StockDataBatchData.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchWithTotalMTRandFinishMTRWithPendingBill extends BatchWithTotalMTRandFinishMTR{
    Boolean pendingBill;

    public BatchWithTotalMTRandFinishMTRWithPendingBill(BatchWithTotalMTRandFinishMTR object , Boolean pendingBill) {
        super(object);
        this.pendingBill = pendingBill;
    }
}
