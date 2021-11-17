package com.main.glory.model.paymentTerm.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

public class MonthlyDispatchPendingReport {

    Double percentageDiscount;
    HashMap<String, List<MonthlyDispatchPendingReportData>> list;


    public MonthlyDispatchPendingReport(DispatchMast dispatchMast) {
        this.percentageDiscount = dispatchMast.getPercentageDiscount();
    }

    public void setList(HashMap<String, List<MonthlyDispatchPendingReportData>> listMap) {
        this.list = listMap;
    }
}
