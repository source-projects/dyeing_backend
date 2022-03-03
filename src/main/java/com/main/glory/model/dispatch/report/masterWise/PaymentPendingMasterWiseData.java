package com.main.glory.model.dispatch.report.masterWise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.party.Party;
import com.main.glory.model.user.UserData;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentPendingMasterWiseData {

    @JsonIgnore
    Party party;
    @JsonIgnore
    UserData masterData;
    Long partyId;
    Double percentageDiscount;
    Double totalNetAmt;
    String monthYear;

    public PaymentPendingMasterWiseData(Party party, UserData masterData, Double percentageDiscount, Double totalNetAmt, String monthYear) {
        this.party = party;
        this.partyId = party.getId();
        this.masterData = masterData;
        this.percentageDiscount = percentageDiscount;
        this.totalNetAmt = StockBatchServiceImpl.changeInFormattedDecimal(totalNetAmt);
        this.monthYear = monthYear;
    }
}
