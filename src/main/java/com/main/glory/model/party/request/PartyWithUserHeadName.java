package com.main.glory.model.party.request;

import com.main.glory.model.party.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class PartyWithUserHeadName extends AddParty {

    String userHeadName;
    public PartyWithUserHeadName(Party party, String userHeadName) {
        super(party);
        this.userHeadName = userHeadName;
    }
}
