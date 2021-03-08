package com.main.glory.model.party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartyWithMasterName extends Party{

    String masterName;

    public PartyWithMasterName(Party p, String masterName) {
        super(p);
        this.masterName = masterName;
    }
}
