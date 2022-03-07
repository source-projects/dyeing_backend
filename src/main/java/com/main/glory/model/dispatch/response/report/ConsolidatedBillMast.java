package com.main.glory.model.dispatch.response.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedBillMast {

    String invoiceNo;
    String createdDate;
    Double totalMtr;
    Double totalFinishMtr;
    Double totalAmt;
    /*String headName;
    Long userHeadId;
    Long partyId;
    Boolean signByParty;*/
    @JsonIgnore
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    List<ConsolidatedBillDataForPDF> list;

    public ConsolidatedBillMast(DispatchMast dispatchMast) {
        this.invoiceNo = dispatchMast.getPostfix();
        this.createdDate =dateFormat.format( dispatchMast.getCreatedDate().toString());
        //this.partyId = dispatchMast.getParty().getId();
    }

    public ConsolidatedBillMast(ConsolidatedBillDataForPDF e) {
        this.invoiceNo = e.getInvoiceNo();
        this.createdDate = dateFormat.format(e.getCreatedDate());
        this.totalMtr = e.totalMtr;
        this.totalAmt = e.getTaxAmt();
        this.totalFinishMtr = e.getTotalFinishMtr();
    }


    public void addTotals(Double totalMtr, Double totalFinishMtr, Double taxAmt) {
        this.totalMtr += totalMtr;
        this.totalFinishMtr += totalFinishMtr;
        this.totalAmt += taxAmt;
    }
}
