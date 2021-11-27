package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDispatchReport {
    String month;
    private Double finishMtr;
    private Double taxAmt;
    private Double discount;
    private int year;
    private Double netAmt;
    String unit;

    public MonthlyDispatchReport( Double finishMtr, Double taxAmt, Double discount, String month, Double netAmt, String unit) {
        this.month = month;
        this.finishMtr = finishMtr;
        this.taxAmt = taxAmt;
        this.discount = discount;
        this.year = year;
        this.netAmt = netAmt;
        this.unit = unit;
    }

    public void addFinishMtr(Double finishMtr){
        this.finishMtr+=finishMtr;
    }

    public void addTaxAmt(Double taxAmt){
        this.taxAmt= this.taxAmt + taxAmt;
    }

    public void addDiscount(Double discount){
        this.discount+=discount;
    }

    public void addNetAmt(Double netAmt){
        this.netAmt+=netAmt;
    }


}
