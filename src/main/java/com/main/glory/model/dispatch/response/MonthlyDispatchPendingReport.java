package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class MonthlyDispatchPendingReport {
    private int month;
    private int year;
    private Double netAmt;
    int partyId;
    private Double discount;
    String partyName;
    Double percentageDiscount;



    public void addDiscount(Double discount){
        this.discount+=discount;
    }

    public void addNetAmt(Double netAmt){
        this.netAmt+=netAmt;
    }

    public MonthlyDispatchPendingReport(int month, int year, Double netAmt, int partyId, Double discount, String partyName) {
        this.month = month;
        this.year = year;
        this.netAmt = netAmt;
        this.partyId = partyId;
        this.discount = discount;
        this.partyName = partyName;
    }

    public MonthlyDispatchPendingReport(int month, int year, Double netAmt, int partyId, Double discount, String partyName, Double percentageDiscount) {
        this.month = month;
        this.year = year;
        this.netAmt = netAmt;
        this.partyId = partyId;
        this.discount = discount;
        this.partyName = partyName;
        this.percentageDiscount = percentageDiscount;
    }
}
