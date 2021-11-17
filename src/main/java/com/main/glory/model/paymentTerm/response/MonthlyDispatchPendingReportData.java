package com.main.glory.model.paymentTerm.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonthlyDispatchPendingReportData {
    private int month;
    private int year;
    private Double netAmt;
    @JsonIgnore
    int partyId;
    String partyName;
    Double discount;
    String headName;


    public MonthlyDispatchPendingReportData(int month, int year, Double netAmt, int partyId, Double discount, String partyName,String headName) {
        this.month = month+year;
        this.year = year;
        this.netAmt = netAmt;
        this.partyId = partyId;
        this.discount = discount;
        this.partyName = partyName;
        this.headName = headName;
    }
}
