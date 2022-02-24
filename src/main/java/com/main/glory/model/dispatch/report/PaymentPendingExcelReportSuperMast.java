package com.main.glory.model.dispatch.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class PaymentPendingExcelReportSuperMast {

    Set<String> monthYearHeader;
    List<PaymentPendingExcelReportMast> list;

    public PaymentPendingExcelReportSuperMast(Set<String> monthYearHeader, List<PaymentPendingExcelReportMast> list) {
        this.monthYearHeader = monthYearHeader;
        this.list = list;
    }
}
