package com.main.glory.model.dispatch.report.masterWise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPendingExcelBasedOnPartyandMasterMast {

    Set<String> monthYearHeader;
    List<PaymentPendingMasterWiseData> list;

}
