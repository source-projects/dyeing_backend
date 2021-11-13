package com.main.glory.model.StockDataBatchData.response;

import com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForExcel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartyBatchReport {
    String partyName;
    String partyCode;
    List<ConsolidatedBillDataForExcel> consolidatedBillDataList;
}
