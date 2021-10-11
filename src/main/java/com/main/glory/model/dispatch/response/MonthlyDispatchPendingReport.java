package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDispatchPendingReport {
    private int month;
    private int year;
    private Double netAmt;


    public void addNetAmt(Double netAmt){
        this.netAmt+=netAmt;
    }


}
