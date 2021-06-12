package com.main.glory.model.dispatch.response;

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

    Long invoiceNo;
    Date createdDate;
    String headName;
    List<ConsolidatedBillData> consolidatedBillDataList;

    public ConsolidatedBillMast(DispatchMast dispatchMast) {
        this.invoiceNo = dispatchMast.getPostfix();
        this.createdDate = dispatchMast.getCreatedDate();
    }
}
