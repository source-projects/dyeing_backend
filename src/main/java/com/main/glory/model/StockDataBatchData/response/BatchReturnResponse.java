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
    Long totalPcs;
    Long chlNo;
    String partyName;
    String qualityName;
    String qualityId;
    List<BatchReturn> batchReturnList;

    public BatchReturnResponse(ReturnBatchDetail e, List<BatchReturn> batchReturnList) {
        this.id = e.getId();
        this.totalMtr = e.getMtr();
        this.totalPcs = Long.valueOf(batchReturnList.size());
        this.chlNo = e.getChlNo();
        this.partyName = e.getPartyName();
        this.qualityName = e.getQualityName();
        this.qualityId = e.getQualityId();
        this.batchReturnList = batchReturnList;
    }
    public BatchReturnResponse(ReturnBatchDetail e,Long size) {
        this.id = e.getId();
        this.totalMtr = e.getMtr();
        this.totalPcs = size;
        this.chlNo = e.getChlNo();
        this.partyName = e.getPartyName();
        this.qualityName = e.getQualityName();
        this.qualityId = e.getQualityId();

    }
}
