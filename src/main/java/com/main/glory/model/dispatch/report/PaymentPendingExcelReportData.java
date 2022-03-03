package com.main.glory.model.dispatch.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.party.Party;
import com.main.glory.model.user.UserData;
import com.main.glory.servicesImpl.DispatchMastImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class PaymentPendingExcelReportData {

    String refNo;//invoiceNo
    String monthYear;
    String date;
    Date createdDate;
    //pendingValue and monthYearValue are both same
    Double pendingValue;
    Double monthYearValue;
    Double percentageDiscount;
    Long dueDays;
    @JsonIgnore
    Party party;

    //@Query("select new com.main.glory.model.dispatch.report.PaymentPendingExcelReportData(dm.postfix," +
    //            "concat(Month(dm.createdDate),', ',Year(dm.createdDate)),dm.createdDate,dm.netAmt,dm.party)" +
    //            "from DispatchMast dm where dm.id in (select dd.controlId from DispatchData dd where (:qualityEntryId IS NULL OR dd.quality.id=:qualityEntryId) AND " +
    //            "(:qualityNameId IS NULL OR dd.quality.qualityName.id=:qualityNameId)) AND " +
    //            "(Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND " +
    //            "(:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) AND (:partyId IS NULL OR dm.party.id=:partyId) AND " +
    //            "dm.paymentBunchId IS NULL")
    public PaymentPendingExcelReportData(String refNo, String monthYear, Date createdDate, Double pendingValue, Double percentageDiscount,Party party) {
        this.refNo = refNo;
        this.monthYear = monthYear;
        this.date = StockBatchServiceImpl.getDateInRespectedDateFormat(createdDate);
        this.pendingValue = pendingValue;
        this.monthYearValue = this.pendingValue;
        this.party = party;
        this.percentageDiscount = percentageDiscount;
        this.createdDate = createdDate;
        this.dueDays = DispatchMastImpl.getDueDaysUsingTwoDate(new Date(),createdDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentPendingExcelReportData paymentPendingExcelReportData = (PaymentPendingExcelReportData) o;
        return monthYear.equalsIgnoreCase(paymentPendingExcelReportData.monthYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monthYear);
    }
}
