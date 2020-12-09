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
    String partyName;
    String partyAddress;
    String GSTIN;
    String qualityId;
    String qualityName;
    Long chlNo;
    Integer pcs;
    Double amt;
    Double totalMtr;
    Double totalFinishMtr;
    String batchId;
    List<BatchData> batchDataList;
}
