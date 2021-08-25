package com.main.glory.model.dispatch.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDispatch {
    Long invoiceNo;
    Long createdBy;
    Long userHeadId;
    Long updatedBy;
    Double discount;
    Double cgst;
    Double sgst;
    Double igst;
    Double taxAmt;
    Double netAmt;
    String password;
    Boolean passwordFlag;
    Double percentageDiscount;
    String remark;
    Boolean createFlag;//true create api else update api
    List<BatchAndStockId> batchAndStockIdList;
}
