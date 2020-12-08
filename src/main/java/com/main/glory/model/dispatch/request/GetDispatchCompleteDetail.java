package com.main.glory.model.dispatch.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetDispatchCompleteDetail {
    Long partyName;
    Long partyAddress;
    String GSTIN;
    String qualityId;
    String qualityName;
    Long chlNo;
    Long pcs;
    Double amt;
    Double totalFinishMtr;
    String batchId;
    List<BatchData> batchDataList;
}
