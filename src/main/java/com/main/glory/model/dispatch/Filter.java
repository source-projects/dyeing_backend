package com.main.glory.model.dispatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Filter {
    String from;
    String to;
    Long userHeadId;
    Long partyId;
    Long qualityEntryId;
    Long qualityNameId;
    Boolean signByParty;

}
