package com.main.glory.model.dispatch.response.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedBillDataForPDF {
    String batchId;
    String headName;
    String partyName;
    String qualityName;
    String qualityId;
    Long pcs;
    Double totalMtr;
    Double totalFinishMtr;
    Double rate;
    Double taxAmt;
    Double sharinkage;
    @JsonIgnore
    String invoiceNo;
    @JsonIgnore
    Date createdDate;


    public ConsolidatedBillDataForPDF(String batchId, Long pcs,Date createdDate,String invoiceNo, String qualityId,
                                      Double totalFinishMtr, Double totalMtr, Double rate,
                                      String qualityName,String partyName, String headName, Double taxAmt
                                      ) {

        this.pcs = pcs;
        this.batchId = batchId;
        this.qualityId = qualityId;
        this.partyName = partyName;
        this.qualityName = qualityName;
        this.headName = headName;
        this.totalMtr = totalMtr;
        this.totalFinishMtr = totalFinishMtr;
        this.rate = rate;
        this.taxAmt = taxAmt;
        this.sharinkage = StockBatchServiceImpl.changeInFormattedDecimal(((this.totalMtr - this.totalFinishMtr) / this.totalMtr) * 100);
        this.invoiceNo = invoiceNo;
        this.createdDate = createdDate;


    }

    public ConsolidatedBillDataForPDF(ConsolidatedBillDataForPDF e) {
        this.pcs = e.pcs;
        this.batchId = e.batchId;
        this.qualityId = e.qualityId;
        this.partyName = e.partyName;
        this.qualityName = e.qualityName;
        this.headName = e.headName;
        this.totalMtr = e.totalMtr;
        this.totalFinishMtr = e.totalFinishMtr;
        this.rate = e.rate;
        this.taxAmt = e.taxAmt;
        this.sharinkage = e.sharinkage;
        this.invoiceNo = e.invoiceNo;
        this.createdDate = e.createdDate;
    }
}
