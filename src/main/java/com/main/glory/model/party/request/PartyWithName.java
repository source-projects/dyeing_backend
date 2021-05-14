package com.main.glory.model.party.request;

import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyWithName {
    private Long id;
    private String partyName;
    String partyCode;
    private Double percentageDiscount;
    Long paymentDays;
    Double creditLimit;
    Double pendingAmt;


    public PartyWithName(Party e,Double pendingAmt) {
        this.id=e.getId();
        this.partyName = e.getPartyName();
        this.partyCode=e.getPartyCode();
        this.percentageDiscount = e.getPercentageDiscount();
        this.paymentDays = e.getPaymentDays();
        this.creditLimit = e.getCreditLimit();
        this.pendingAmt = pendingAmt==null?0.0:pendingAmt;
    }
    public PartyWithName(Party e) {
        this.id=e.getId();
        this.partyName = e.getPartyName();
        this.partyCode=e.getPartyCode();
        this.percentageDiscount = e.getPercentageDiscount();
        this.paymentDays = e.getPaymentDays();
        this.creditLimit = e.getCreditLimit();

    }

}
