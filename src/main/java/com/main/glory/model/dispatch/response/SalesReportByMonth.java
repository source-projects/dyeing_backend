package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class SalesReportByMonth {
    Integer month;
    Double finishMtr;
    Double taxAmt;
    Double discount;
    Double netAmt;

    public SalesReportByMonth(Integer month, Double finishMtr, Double taxAmt, Double discount, Double netAmt) {
        this.month = month;
        this.finishMtr = finishMtr;
        this.taxAmt = taxAmt;
        this.discount = discount;
        this.netAmt = netAmt;
    }
}
