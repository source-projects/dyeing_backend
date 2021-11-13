package com.main.glory.model.dispatch.response.report;

import com.main.glory.model.dispatch.DispatchMast;
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
public class ConsolidatedBillMast {

    String invoiceNo;
    Date createdDate;
    /*String headName;
    Long userHeadId;
    Long partyId;
    Boolean signByParty;*/
    List<ConsolidatedBillDataForPDF> consolidatedBillDataForPDFS;

    public ConsolidatedBillMast(DispatchMast dispatchMast) {
        this.invoiceNo = dispatchMast.getPostfix();
        this.createdDate = dispatchMast.getCreatedDate();
        //this.partyId = dispatchMast.getParty().getId();
    }

    public ConsolidatedBillMast(ConsolidatedBillDataForPDF e) {
        this.invoiceNo = e.getInvoiceNo();
        this.createdDate = e.getCreatedDate();

    }
}
