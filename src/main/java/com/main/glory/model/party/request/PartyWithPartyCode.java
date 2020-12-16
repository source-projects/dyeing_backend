package com.main.glory.model.party.request;

import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartyWithPartyCode {
    Long partyId;
    String partyName;
    String partyCode;

    public PartyWithPartyCode(Party party) {
        this.partyCode=party.getPartyCode();
        this.partyName=party.getPartyName();
        this.partyId=party.getId();

    }
}
