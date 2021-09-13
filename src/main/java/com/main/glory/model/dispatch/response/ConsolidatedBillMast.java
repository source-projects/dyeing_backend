package com.main.glory.model.dispatch.response;

import com.main.glory.model.dispatch.DispatchMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsolidatedBillMast {

    Long invoiceNo;
    Date createdDate;
    String headName;
    Long userHeadId;
    Long partyId;
    Boolean signByParty;
    List<ConsolidatedBillData> consolidatedBillDataList;

    public ConsolidatedBillMast(DispatchMast dispatchMast) {
        this.invoiceNo = dispatchMast.getPostfix();
        this.createdDate = dispatchMast.getCreatedDate();
        this.partyId = dispatchMast.getParty().getId();
    }

}
