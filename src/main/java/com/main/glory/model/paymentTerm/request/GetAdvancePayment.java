package com.main.glory.model.paymentTerm.request;

import com.main.glory.model.party.Party;
import com.main.glory.model.paymentTerm.AdvancePayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAdvancePayment {

    Long id;
    String partyName;
    Long partyId;
    Double amt;
    String date;
    String remark;

    public GetAdvancePayment(AdvancePayment advancePayment, Party party) {
        this.id=advancePayment.getId();
        this.amt=advancePayment.getAmt();
        this.partyId=advancePayment.getPartyId();
        this.date=advancePayment.getCreatedDate().toString();
        this.remark=advancePayment.getRemark();
        this.partyName=party.getPartyName();

    }
}
