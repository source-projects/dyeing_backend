package com.main.glory.model.dispatch.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.party.Party;
import com.main.glory.model.user.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPendingExcelReportMast {

    @JsonIgnore
    Long partyId;
    String partyCode;
    String partyName;
    Long paymentDays;
    String partyAddress;
    @JsonIgnore
    UserData masterData;

    List<PaymentPendingExcelReportData> list;

    public PaymentPendingExcelReportMast(Party key, List<PaymentPendingExcelReportData> value) {
        this.partyId = key.getId();
        this.partyCode = key.getPartyCode();
        this.partyName = key.getPartyName();
        this.paymentDays = key.getPaymentDays();
        this.partyAddress = key.getPartyAddress1()==null?null:key.getPartyAddress1();
        this.masterData = key.getUserHeadData();
        this.list = value;
    }
}
