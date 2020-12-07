package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.model.StockDataBatchData.BatchData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetCompleteFinishMtrDetail {
    Long partyId;
    String partyName;
    Long userHeadId;
    String masterName;
    Long controlId;
    String batchId;
    Integer pcs;
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    Date date;
    Double totalMtr;
    Double totalWt;
    List<BatchData> batchData;
}
