package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDispatchReport {
    private int month;
    private Double finishMtr;
    private Double taxAmt;
    private Double discount;
    private int year;
    private Double netAmt;
    String unit;

    public void addFinishMtr(Double finishMtr){
        this.finishMtr+=finishMtr;
    }

    public void addTaxAmt(Double taxAmt){
        this.taxAmt+=taxAmt;
    }

    public void addDiscount(Double discount){
        this.discount+=discount;
    }

    public void addNetAmt(Double netAmt){
        this.netAmt+=netAmt;
    }


}
