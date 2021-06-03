package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.StockDataBatchData.BatchReturn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchReturnResponse {
    Long id;
    Double totalMtr;
    Long chlNo;
    String partyName;
    String qualityName;
    String qualityId;
    List<BatchReturn> batchReturnList;



}
