package com.main.glory.model.StockDataBatchData.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingBatchDataForExcel {
    String partyName;
    String partyCode;
    String masterName;
    String qualityId;
    String qualityName;
    String pchallanRef;
    String batchId;
    Long totalPcs;
    String receiveDate;
    Double totalBatchMtr;
    Double totalBatchWt;

    @JsonIgnore
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public PendingBatchDataForExcel(String partyName, String partyCode, String masterName, String qualityId, String qualityName, String pchallanRef, String batchId, Long totalPcs, String receiveDate, Double totalBatchMtr, Double totalBatchWt) {
        this.partyName = partyName;
        this.partyCode = partyCode;
        this.masterName = masterName;
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.pchallanRef = pchallanRef;
        this.batchId = batchId;
        this.totalPcs = totalPcs;
        this.receiveDate = receiveDate;
        this.totalBatchMtr = totalBatchMtr;
        this.totalBatchWt = totalBatchWt;

    }
    public PendingBatchDataForExcel(String partyName, String partyCode, String masterName, String qualityId, Long count, Double totalBatchMtr, Double totalBatchWt, String batchId, String pchallanRef, Date receiveDate,String qualityName) {
        this.partyName = partyName;
        this.partyCode = partyCode;
        this.totalBatchWt = totalBatchWt;
        this.totalBatchMtr = totalBatchMtr;
        this.totalPcs = count;
        this.masterName=masterName;
        this.qualityId=qualityId;
        this.pchallanRef=pchallanRef;
        this.batchId=batchId;
        this.receiveDate = formatter.format(receiveDate);
        this.qualityName = qualityName;

    }
}
