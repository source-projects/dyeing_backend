package com.main.glory.model.party.request;

import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyWithName {
    private Long id;
    private String partyName;
    String partyCode;
    private Double percentageDiscount;

    public PartyWithName(Party e) {
        this.id=e.getId();
        this.partyName = e.getPartyName();
        this.partyCode=e.getPartyCode();
        this.percentageDiscount = e.getPercentageDiscount();
    }
}
